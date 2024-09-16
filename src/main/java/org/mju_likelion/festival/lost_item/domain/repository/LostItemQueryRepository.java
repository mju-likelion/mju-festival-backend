package org.mju_likelion.festival.lost_item.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;
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
          rs.getTimestamp("createdAt").toLocalDateTime(),
          rs.getBoolean("isFounded")
      );
    };
  }

  /**
   * 페이지네이션을 적용하여 분실물 간단 정보 List 조회.
   *
   * @param sortOrder 정렬
   * @param page      페이지
   * @param size      크기
   * @return 분실물 간단 정보 List
   */
  public List<SimpleLostItem> findOrderedSimpleLostItemsWithPagenation(
      final SortOrder sortOrder,
      final int page,
      final int size) {

    String sql =
        "SELECT HEX(li.id) AS lostItemId, li.title AS title, li.content AS content, "
            + "CASE WHEN li.retriever_info IS NULL THEN FALSE ELSE TRUE END AS isFounded, "
            + "i.url AS imageUrl, li.created_at AS createdAt "
            + "FROM lost_item li "
            + "INNER JOIN image i ON li.image_id = i.id "
            + "ORDER BY li.created_at " + sortOrder.toString() + " "
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

  /**
   * 페이지네이션을 적용하여 분실물 검색.
   *
   * @param sortOrder 정렬
   * @param keyword   검색어
   * @param page      페이지
   * @param size      크기
   * @return 검색 결과
   */
  public List<SimpleLostItem> findOrderedSimpleLostItemsWithPagenationByKeyword(
      final SortOrder sortOrder,
      final String keyword,
      final int page,
      final int size) {

    String sql = "SELECT HEX(li.id) AS lostItemId, li.title AS title, li.content AS content, "
        + "CASE WHEN li.retriever_info IS NULL THEN FALSE ELSE TRUE END AS isFounded, "
        + "i.url AS imageUrl, li.created_at AS createdAt "
        + "FROM lost_item li "
        + "INNER JOIN image i ON li.image_id = i.id "
        + "WHERE li.title LIKE :keyword OR li.content LIKE :keyword "
        + "ORDER BY li.created_at " + sortOrder.name() + " "
        + "LIMIT :limit OFFSET :offset";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("keyword", "%" + keyword + "%")
        .addValue("limit", size)
        .addValue("offset", page * size);

    return jdbcTemplate.query(sql, params, simpleLostItemRowMapper);
  }

  /**
   * 키워드에 해당하는 총 페이지 수 조회.
   *
   * @param keyword 키워드
   * @param size    크기
   * @return 총 페이지 수
   */
  public int findTotalPageByKeyword(final String keyword, final int size) {
    String sql = "SELECT CEIL(COUNT(*) / :size) "
        + "FROM lost_item "
        + "WHERE title LIKE :keyword OR content LIKE :keyword";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("keyword", "%" + keyword + "%")
        .addValue("size", size);

    return jdbcTemplate.queryForObject(sql, params, Integer.class);
  }
}
