package org.mju_likelion.festival.term.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.hexToUUID;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.term.domain.SimpleTerm;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TermQueryRepository {

  private final RowMapper<SimpleTerm> simpleTermRowMapper = simpleTermRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<SimpleTerm> simpleTermRowMapper() {
    return (rs, rowNum) -> {
      String hexId = rs.getString("termId");
      UUID uuid = hexToUUID(hexId);
      return new SimpleTerm(
          uuid,
          rs.getString("termTitle"),
          rs.getString("termContent")
      );
    };
  }

  /**
   * Term 의 Order 를 기준으로 오름차순으로 정렬하여 모든 Term 조회.
   *
   * @return Term 의 Order 를 기준으로 오름차순으로 정렬된 모든 Term
   */
  public List<SimpleTerm> findTermsByOrderBySequenceAsc() {
    String sql = "SELECT HEX(id) AS termId, title AS termTitle, content AS termContent "
        + "FROM term ORDER BY sequence ASC";
    return jdbcTemplate.query(sql, simpleTermRowMapper);
  }
}
