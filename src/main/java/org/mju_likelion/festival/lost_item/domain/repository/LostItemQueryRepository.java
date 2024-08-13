package org.mju_likelion.festival.lost_item.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LostItemQueryRepository {

  private final RowMapper<SimpleLostItem> simpleLostItemRowMapper = simpleLostItemRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<SimpleLostItem> simpleLostItemRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("lostItemId");
      UUID uuid = hexToUUID(hexId);
      return new SimpleLostItem(
          uuid,
          rs.getString("title"),
          rs.getString("content"),
          rs.getString("imageUrl"),
          rs.getTimestamp("createdAt").toLocalDateTime()
      );
    };
  }

  /**
   * 페이지네이션을 적용하여 분실물 간단 정보 List 조회.
   *
   * @param direction 정렬
   * @param page      페이지
   * @param size      크기
   * @return 분실물 간단 정보 List
   */
  public List<SimpleLostItem> findOrderedSimpleLostItemsWithPagenation(final Direction direction,
      final int page,
      final int size) {
    String sql =
        "SELECT HEX(li.id) AS lostItemId, li.title AS title, li.content AS content, "
            + "i.url AS imageUrl, li.created_at AS createdAt "
            + "FROM lost_item li "
            + "LEFT JOIN image i ON li.image_id = i.id "
            + "ORDER BY li.created_at " + direction.toString() + " "
            + "LIMIT :limit OFFSET :offset";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("limit", size)
        .addValue("offset", page * size);

    return jdbcTemplate.query(sql, params, simpleLostItemRowMapper);
  }

  /**
   * 총 페이지 수 조회.
   *
   * @param size 크기
   * @return 총 페이지 수
   */
  public int findTotalPage(final int size) {
    String sql = "SELECT CEIL(COUNT(*) / :size) FROM lost_item";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("size", size);

    return jdbcTemplate.queryForObject(sql, params, Integer.class);
  }
}
