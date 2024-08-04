package org.mju_likelion.festival.booth.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;
import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.uuidToHex;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothDetail;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 부스 조회 Repository.
 */
@Repository
@RequiredArgsConstructor
public class BoothQueryRepository {

  private final RowMapper<SimpleBooth> simpleBoothRowMapper = simpleBoothRowMapper();
  private final RowMapper<BoothDetail> boothDetailRowMapper = boothDetailRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<SimpleBooth> simpleBoothRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("boothId");
      UUID uuid = hexToUUID(hexId);
      return new SimpleBooth(
          uuid,
          rs.getString("boothName"),
          rs.getString("boothDescription"),
          rs.getString("thumbnailUrl")
      );
    };
  }

  private RowMapper<BoothDetail> boothDetailRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("boothId");
      UUID uuid = hexToUUID(hexId);
      return new BoothDetail(
          uuid,
          rs.getString("boothName"),
          rs.getString("boothDescription"),
          rs.getString("boothLocation"),
          rs.getString("thumbnailUrl"),
          List.of(rs.getString("imageUrl")),
          rs.getTimestamp("createdAt").toLocalDateTime()
      );
    };
  }

  /**
   * 페이지네이션을 적용하여 부스 간단 정보 List 조회.
   *
   * @param page 페이지
   * @param size 크기
   * @return 부스 간단 정보 List
   */
  public List<SimpleBooth> findOrderedSimpleBoothsWithPagination(final int page, final int size) {
    String sql =
        "SELECT HEX(b.id) AS boothId, b.name AS boothName, b.description AS boothDescription, "
            + "i.url AS thumbnailUrl "
            + "FROM booth b "
            + "LEFT JOIN image i ON b.thumbnail_id = i.id "
            + "ORDER BY b.sequence ASC "
            + "LIMIT :limit OFFSET :offset";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("limit", size)
        .addValue("offset", page * size);

    return jdbcTemplate.query(sql, params, simpleBoothRowMapper);
  }

  /**
   * 부스 상세 정보 조회.
   *
   * @param id 부스 ID
   * @return 부스 상세 정보
   */
  public Optional<BoothDetail> findBoothById(final UUID id) {
    String sql =
        "SELECT HEX(b.id) AS boothId, b.name AS boothName, b.description AS boothDescription, "
            + "b.location AS boothLocation, i.url AS imageUrl, ti.url AS thumbnailUrl, "
            + "b.created_at AS createdAt "
            + "FROM booth b "
            + "LEFT JOIN booth_image bi ON b.id = bi.booth_id "
            + "LEFT JOIN image i ON bi.image_id = i.id "
            + "LEFT JOIN image ti ON b.thumbnail_id = ti.id "
            + "WHERE b.id = UNHEX(:id)";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", uuidToHex(id));

    return Optional
        .ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, boothDetailRowMapper)));
  }

  /**
   * 부스 존재 여부 조회.
   *
   * @param id 부스 ID
   * @return 부스 존재 여부
   */
  public boolean existsById(final UUID id) {
    String sql = "SELECT EXISTS(SELECT 1 FROM booth WHERE id = UNHEX(:id))";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", uuidToHex(id));

    int count = jdbcTemplate.queryForObject(sql, params, Integer.class);
    return count > 0;
  }
}
