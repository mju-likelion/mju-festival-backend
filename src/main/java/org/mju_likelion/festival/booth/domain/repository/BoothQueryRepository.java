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
          rs.getString("imageUrl")
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
          rs.getString("department"),
          rs.getString("boothLocation"),
          rs.getString("imageUrl"),
          rs.getString("locationImageUrl"),
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
            + "i.url AS imageUrl "
            + "FROM booth b "
            + "LEFT JOIN image i ON b.image_id = i.id "
            + "ORDER BY b.sequence ASC "
            + "LIMIT :limit OFFSET :offset";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("limit", size)
        .addValue("offset", page * size);

    return jdbcTemplate.query(sql, params, simpleBoothRowMapper);
  }

  /**
   * 페이지네이션 시 부스 간단 정보 List 총 페이지 수 조회.
   *
   * @param size 크기
   * @return 총 페이지 수
   */
  public int findTotalPage(final int size) {
    String sql = "SELECT CEIL(COUNT(*) / :size) FROM booth";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("size", size);

    return jdbcTemplate.queryForObject(sql, params, Integer.class);
  }

  /**
   * 부스 ID 와 관리자 ID 로 부스 소유 여부 확인.
   *
   * @param boothId 부스 ID
   * @param adminId 관리자 ID
   * @return 부스 소유 여부
   */
  public boolean isBoothOwner(final UUID boothId, final UUID adminId) {
    String sql =
        "SELECT COUNT(*) "
            + "FROM booth b "
            + "JOIN admin a ON b.owner_id = a.id "
            + "WHERE b.id = UNHEX(:boothId) AND a.id = UNHEX(:adminId)";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("boothId", uuidToHex(boothId))
        .addValue("adminId", uuidToHex(adminId));

    return jdbcTemplate.queryForObject(sql, params, Integer.class) > 0;
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
            + "bd.name AS department, b.location AS boothLocation, i.url AS imageUrl, li.url AS locationImageUrl, "
            + "b.created_at AS createdAt "
            + "FROM booth b "
            + "INNER JOIN image i ON b.image_id = i.id "
            + "INNER JOIN image li ON b.location_image_id = li.id "
            + "INNER JOIN booth_department bd ON b.department_id = bd.id "
            + "WHERE b.id = UNHEX(:id)";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", uuidToHex(id));

    return Optional
        .ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, boothDetailRowMapper)));
  }
}
