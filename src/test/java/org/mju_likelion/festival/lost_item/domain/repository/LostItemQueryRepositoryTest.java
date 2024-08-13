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
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.AdminRole;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.image.domain.Image;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("LostItemQueryRepository")
@ApplicationTest
@Transactional
public class LostItemQueryRepositoryTest {

  @Autowired
  private LostItemQueryRepository lostItemQueryRepository;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private LostItemJpaRepository lostItemJpaRepository;

  @Autowired
  private AdminJpaRepository adminJpaRepository;

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
          SortOrder.ASC, page, pageSize);

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
          SortOrder.DESC, page, pageSize);

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

  @DisplayName("제목에 포함된 키워드를 통해 분실물 간단 정보 List 조회")
  @Test
  void testFindOrderedSimpleLostItemsWithKeywordAndPagination() {
    // given
    String keyword = "지갑";
    int pageSize = 1;
    int totalPages = 1;
    Admin admin = new Admin("lost_item_admin", "1234", "분실물 관리자", AdminRole.STUDENT_COUNCIL, null,
        null);
    adminJpaRepository.saveAndFlush(admin);
    LostItem lostItem = new LostItem(admin, "지갑 발견", "발견했습니다.", null, new Image("asdf"));
    lostItemJpaRepository.saveAndFlush(lostItem);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleLostItem> pageContent = lostItemQueryRepository.findOrderedSimpleLostItemsWithPagenationByKeyword(
          SortOrder.ASC, keyword, page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalLostItemNum);

      assertAll(
          () -> {
            assertThat(pageContent).hasSize(expectedSize);
            assertThat(pageContent).isSortedAccordingTo(
                Comparator.comparing(SimpleLostItem::getCreatedAt));
          }
      );
    });

    // clean up
    adminJpaRepository.delete(admin);
  }

  @DisplayName("본문에 포함된 키워드를 통해 분실물 간단 정보 List 조회")
  @Test
  void testFindOrderedSimpleLostItemsWithContentKeywordAndPagination() {
    // given
    String keyword = "지갑";
    int pageSize = 1;
    int totalPages = 1;
    Admin admin = new Admin("lost_item_admin", "1234", "분실물 관리자", AdminRole.STUDENT_COUNCIL, null,
        null);
    adminJpaRepository.saveAndFlush(admin);
    LostItem lostItem = new LostItem(admin, "발견", "지갑 발견했습니다.", null, new Image("asdf"));
    lostItemJpaRepository.saveAndFlush(lostItem);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleLostItem> pageContent = lostItemQueryRepository.findOrderedSimpleLostItemsWithPagenationByKeyword(
          SortOrder.ASC, keyword, page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalLostItemNum);

      assertAll(
          () -> {
            assertThat(pageContent).hasSize(expectedSize);
            assertThat(pageContent).isSortedAccordingTo(
                Comparator.comparing(SimpleLostItem::getCreatedAt));
          }
      );
    });

    // clean up
    adminJpaRepository.delete(admin);
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
