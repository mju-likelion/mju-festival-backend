package org.mju_likelion.festival.user.domain.repository;

import static org.mju_likelion.festival.common.util.uuid.UUIDUtil.uuidToHex;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.user.domain.UserStamp;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

  private final RowMapper<UserStamp> userStampRowMapper = userStampRowMapper();
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private RowMapper<UserStamp> userStampRowMapper() {
    return (rs, rowNum) -> new UserStamp(
        rs.getInt("totalStampCount"),
        rs.getInt("stampCount")
    );
  }

  /**
   * 사용자의 스탬프 정보 조회.
   *
   * @param userId 사용자 ID
   * @return 사용자의 스탬프 정보
   */
  public UserStamp findUserStampByUserId(final UUID userId) {
    String sql = "SELECT "
        + "  (SELECT COUNT(*) FROM booth) AS totalStampCount,"
        + "  (SELECT COUNT(*) FROM booth_user WHERE user_id = UNHEX(:userId)) AS stampCount";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("userId", uuidToHex(userId));

    return jdbcTemplate.queryForObject(sql, params, userStampRowMapper);
  }
}
