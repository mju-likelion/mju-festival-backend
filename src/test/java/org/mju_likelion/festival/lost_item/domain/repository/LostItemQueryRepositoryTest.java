package org.mju_likelion.festival.lost_item.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("LostItemQueryRepository")
@ApplicationTest
@Transactional(readOnly = true)
public class LostItemQueryRepositoryTest {

  @Autowired
  private LostItemQueryRepository lostItemQueryRepository;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private LostItemJpaRepository lostItemJpaRepository;

  private int totalLostItemNum;

  @BeforeEach
  void setUp() {
    lostItemQueryRepository = new LostItemQueryRepository(
        new NamedParameterJdbcTemplate(dataSource));
    totalLostItemNum = (int) lostItemJpaRepository.count();
  }

  @DisplayName("분실물 간단 정보 List 조회 - 페이지네이션 ( 생성 시간 오름차순 )")
  @Test
  void testFindOrderedSimpleLostItemsWithPagination() {
    // given
    int pageSize = 5;
    int totalPages = calculateTotalPages(totalLostItemNum, pageSize);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleLostItem> pageContent = lostItemQueryRepository.findOrderedSimpleLostItemsWithPagenation(
          Direction.ASC, page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalLostItemNum);

      assertAll(
          () -> {
            assertThat(pageContent).hasSize(expectedSize);
            assertThat(pageContent).isSortedAccordingTo(
                Comparator.comparing(SimpleLostItem::getCreatedAt));
          }
      );
    });
  }

  @DisplayName("분실물 간단 정보 List 조회 - 페이지네이션 ( 생성 시간 내림차순 )")
  @Test
  void testFindOrderedSimpleLostItemsWithPaginationDesc() {
    // given
    int pageSize = 5;
    int totalPages = calculateTotalPages(totalLostItemNum, pageSize);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleLostItem> pageContent = lostItemQueryRepository.findOrderedSimpleLostItemsWithPagenation(
          Direction.DESC, page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalLostItemNum);

      assertAll(
          () -> {
            assertThat(pageContent).hasSize(expectedSize);
            assertThat(pageContent).isSortedAccordingTo(
                Comparator.comparing(SimpleLostItem::getCreatedAt).reversed());
          }
      );
    });
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
