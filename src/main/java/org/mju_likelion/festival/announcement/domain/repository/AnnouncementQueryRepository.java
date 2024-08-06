package org.mju_likelion.festival.announcement.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnnouncementQueryRepository {

  private final RowMapper<SimpleAnnouncement> simpleAnnouncementRowMapper = simpleAnnouncementRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<SimpleAnnouncement> simpleAnnouncementRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("announcementId");
      UUID uuid = hexToUUID(hexId);
      return new SimpleAnnouncement(
          uuid,
          rs.getString("title"),
          rs.getString("content")
      );
    };
  }

  /**
   * 페이지네이션을 적용하여 공지사항 간단 정보 List 조회.
   *
   * @param direction 정렬
   * @param page      페이지
   * @param size      크기
   * @return 공지사항 간단 정보 List
   */
  public List<SimpleAnnouncement> findSimpleAnnouncements(Direction direction, final int page,
      final int size) {
    String sql = "SELECT HEX(a.id) AS announcementId, a.title AS title, a.content AS content "
        + "FROM announcement a "
        + "ORDER BY a.created_at " + direction.toString() + " "
        + "LIMIT :limit OFFSET :offset";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("limit", size)
        .addValue("offset", page * size);

    return jdbcTemplate.query(sql, params, simpleAnnouncementRowMapper);
  }
}
