package org.mju_likelion.festival.announcement.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;
import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.uuidToHex;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.announcement.domain.AnnouncementDetail;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnnouncementQueryRepository {

  private final RowMapper<SimpleAnnouncement> simpleAnnouncementRowMapper = simpleAnnouncementRowMapper();
  private final RowMapper<AnnouncementDetail> announcementDetailRowMapper = announcementDetailRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<SimpleAnnouncement> simpleAnnouncementRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("announcementId");
      UUID uuid = hexToUUID(hexId);
      return new SimpleAnnouncement(
          uuid,
          rs.getString("title"),
          rs.getString("content"),
          rs.getTimestamp("createdAt").toLocalDateTime()
      );
    };
  }

  private RowMapper<AnnouncementDetail> announcementDetailRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("announcementId");
      UUID uuid = hexToUUID(hexId);
      return new AnnouncementDetail(
          uuid,
          rs.getString("title"),
          rs.getString("content"),
          rs.getTimestamp("createdAt").toLocalDateTime(),
          rs.getString("imageUrl")
      );
    };
  }

  /**
   * 페이지네이션을 적용하여 공지사항 간단 정보 List 조회.
   *
   * @param sortOrder 정렬
   * @param page      페이지
   * @param size      크기
   * @return 공지사항 간단 정보 List
   */
  public List<SimpleAnnouncement> findOrderedSimpleAnnouncementsWithPagenation(
      final SortOrder sortOrder,
      final int page,
      final int size) {

    String sql =
        "SELECT HEX(a.id) AS announcementId, a.title AS title, a.content AS content, a.created_at AS createdAt "
            + "FROM announcement a "
            + "ORDER BY a.created_at " + sortOrder.toString() + " "
            + "LIMIT :limit OFFSET :offset";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("limit", size)
        .addValue("offset", page * size);

    return jdbcTemplate.query(sql, params, simpleAnnouncementRowMapper);
  }

  /**
   * 공지사항 상세 정보 조회.
   *
   * @param id 공지사항 ID
   */
  public Optional<AnnouncementDetail> findAnnouncementById(final UUID id) {
    String sql =
        "SELECT HEX(a.id) AS announcementId, a.title AS title, a.content AS content, "
            + "a.created_at AS createdAt, i.url AS imageUrl "
            + "FROM announcement a "
            + "LEFT JOIN image i ON a.image_id = i.id "
            + "WHERE a.id = UNHEX(:id)";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", uuidToHex(id));

    return Optional
        .ofNullable(
            DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, params, announcementDetailRowMapper)));
  }

  /**
   * 페이지 크기를 기준으로 총 페이지 수 조회.
   *
   * @param size 크기
   * @return 총 페이지 수
   */
  public int getTotalPage(final int size) {
    String sql = "SELECT CEIL(COUNT(*) / :size) "
        + "FROM announcement";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("size", size);

    return jdbcTemplate.queryForObject(sql, params, Integer.class);
  }
}
