package org.mju_likelion.festival.booth.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothDetail;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("BoothQueryRepository")
@ApplicationTest
@Transactional(readOnly = true)
public class BoothQueryRepositoryTest {

  @Autowired
  private BoothJpaRepository boothJpaRepository;

  @Autowired
  private DataSource dataSource;

  private BoothQueryRepository boothQueryRepository;

  private int totalBoothNum;

  @BeforeEach
  void setUp() {
    boothQueryRepository = new BoothQueryRepository(new NamedParameterJdbcTemplate(dataSource));
    totalBoothNum = (int) boothJpaRepository.count();
  }

  @DisplayName("부스 간단 정보 List 조회 - 페이지네이션")
  @Test
  void testFindOrderedSimpleBoothsWithPagination() {
    // given
    int pageSize = 5;
    int totalPages = calculateTotalPages(totalBoothNum, pageSize);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleBooth> pageContent = boothQueryRepository.findOrderedSimpleBoothsWithPagination(
          page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalBoothNum);

      assertThat(pageContent)
          .hasSize(expectedSize)
          .withFailMessage("For page %d, expected size %d but got %d", page, expectedSize,
              pageContent.size());
    });
  }

  @DisplayName("부스 상세 정보 조회")
  @Test
  void testFindBoothDetail() {
    // given
    Booth booth = boothJpaRepository.findAll().get(0);
    // when
    Optional<BoothDetail> optionalBoothDetail = boothQueryRepository.findBoothById(booth.getId());
    // then
    assertThat(optionalBoothDetail)
        .isPresent()
        .hasValueSatisfying(boothDetail ->
            assertThat(boothDetail.getId()).isEqualTo(booth.getId())
        );
  }

  private int calculateTotalPages(int totalItems, int pageSize) {
    return (totalItems + pageSize - 1) / pageSize;
  }

  private int calculateExpectedSize(int page, int pageSize, int totalBoothNum) {
    if (isLastPage(page, totalBoothNum, pageSize)) {
      return totalBoothNum % pageSize == 0 ? pageSize : totalBoothNum % pageSize;
    }
    return pageSize;
  }

  private boolean isLastPage(int page, int totalBoothNum, int pageSize) {
    return page == calculateTotalPages(totalBoothNum, pageSize) - 1;
  }
}