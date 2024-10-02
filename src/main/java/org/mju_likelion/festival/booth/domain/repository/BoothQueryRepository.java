package org.mju_likelion.festival.booth.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;
import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.uuidToHex;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothDetail;
import org.mju_likelion.festival.booth.domain.BoothNames;
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
  private final RowMapper<String> boothNameRowMapper = boothNameRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<SimpleBooth> simpleBoothRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("boothId");
      UUID uuid = hexToUUID(hexId);
      return new SimpleBooth(
          uuid,
          rs.getString("departmentName"),
          rs.getString("boothName"),
          rs.getString("imageUrl"),
          rs.getBoolean("isEventBooth")
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
          rs.getBoolean("isEventBooth"),
          rs.getTimestamp("createdAt").toLocalDateTime()
      );
    };
  }

  private RowMapper<String> boothNameRowMapper() {
    return (rs, rowNum) -> rs.getString("boothName");
  }

  /**
   * 부스 소속 ID 에 해당하는 부스들 조회.
   *
   * @param affiliationId 부스 소속 ID
   * @return 부스 간단 정보 List
   */
  public List<SimpleBooth> findAllSimpleBoothByAffiliationId(final UUID affiliationId) {

    String sql =
        "SELECT HEX(b.id) AS boothId, bd.name AS departmentName, b.name AS boothName, "
            + "i.url AS imageUrl, b.is_event_booth AS isEventBooth "
            + "FROM booth b "
            + "INNER JOIN image i ON b.image_id = i.id "
            + "INNER JOIN booth_department bd ON b.department_id = bd.id "
            + "WHERE b.affiliation_id = UNHEX(:affiliationId) "
            + "ORDER BY b.sequence ASC ";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("affiliationId", uuidToHex(affiliationId));

    return jdbcTemplate.query(sql, params, simpleBoothRowMapper);
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
        "SELECT EXISTS("
            + "SELECT 1 FROM booth b "
            + "WHERE b.id = UNHEX(:boothId) AND b.owner_id = UNHEX(:adminId)"
            + ") AS isOwner";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("boothId", uuidToHex(boothId))
        .addValue("adminId", uuidToHex(adminId));

    return jdbcTemplate.queryForObject(sql, params, Integer.class) > 0;
  }

  /**
   * 이벤트 부스인지 확인.
   *
   * @param boothId 부스 ID
   * @return 이벤트 부스 여부
   */
  public boolean isEventBooth(final UUID boothId) {
    String sql =
        "SELECT EXISTS("
            + "SELECT 1 FROM booth b "
            + "WHERE b.id = UNHEX(:boothId) AND b.is_event_booth = TRUE"
            + ") AS isEventBooth";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("boothId", uuidToHex(boothId));

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
            + "b.is_event_booth AS isEventBooth, b.created_at AS createdAt "
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

  /**
   * 사용자가 참여한 부스 이름 전체 조회.
   *
   * @param userId 사용자 ID
   * @return 부스 이름 List
   */
  public BoothNames findBoothNamesSortedByCreatedAt(final UUID userId) {
    String sql =
        "SELECT b.name AS boothName "
            + "FROM booth b "
            + "INNER JOIN booth_user bu ON b.id = bu.booth_id "
            + "WHERE bu.user_id = UNHEX(:userId) "
            + "ORDER BY bu.created_at ASC ";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("userId", uuidToHex(userId));

    List<String> boothNames = jdbcTemplate.query(sql, params, boothNameRowMapper);
    return new BoothNames(boothNames);
  }
}
