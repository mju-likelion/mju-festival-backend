package org.mju_likelion.festival.announcement.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.announcement.domain.AnnouncementDetail;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.util.enums.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("AnnouncementQueryRepository")
@ApplicationTest
@Transactional(readOnly = true)
public class AnnouncementQueryRepositoryTest {

  @Autowired
  private AnnouncementJpaRepository announcementJpaRepository;

  @Autowired
  private DataSource dataSource;

  private AnnouncementQueryRepository announcementQueryRepository;

  private int totalAnnouncementNum;

  @BeforeEach
  void setUp() {
    announcementQueryRepository = new AnnouncementQueryRepository(
        new NamedParameterJdbcTemplate(dataSource));
    totalAnnouncementNum = (int) announcementJpaRepository.count();
  }

  @DisplayName("공지사항 간단 정보 List 조회 - 페이지네이션 ( 생성 시간 오름차순 )")
  @Test
  void testFindOrderedSimpleAnnouncementsWithPagination() {
    // given
    int pageSize = 5;
    int totalPages = calculateTotalPages(totalAnnouncementNum, pageSize);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleAnnouncement> pageContent = announcementQueryRepository.findOrderedSimpleAnnouncementsWithPagenation(
          SortOrder.ASC, page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalAnnouncementNum);

      assertAll(
          () -> {
            assertThat(pageContent).isSortedAccordingTo(
                Comparator.comparing(SimpleAnnouncement::getCreatedAt));

            assertThat(pageContent)
                .hasSize(expectedSize)
                .withFailMessage("For page %d, expected size %d but got %d", page, expectedSize,
                    pageContent.size());
          }
      );
    });
  }

  @DisplayName("공지사항 간단 정보 List 조회 - 페이지네이션 ( 생성 시간 내림차순 )")
  @Test
  void testFindOrderedSimpleAnnouncementsWithPaginationDesc() {
    // given
    int pageSize = 5;
    int totalPages = calculateTotalPages(totalAnnouncementNum, pageSize);

    // when & then
    IntStream.range(0, totalPages).forEach(page -> {
      List<SimpleAnnouncement> pageContent = announcementQueryRepository.findOrderedSimpleAnnouncementsWithPagenation(
          SortOrder.DESC, page, pageSize);

      int expectedSize = calculateExpectedSize(page, pageSize, totalAnnouncementNum);

      assertAll(
          () -> {
            assertThat(pageContent).isSortedAccordingTo(
                (a, b) -> b.getCreatedAt()
                    .compareTo(a.getCreatedAt()));

            assertThat(pageContent)
                .hasSize(expectedSize)
                .withFailMessage("For page %d, expected size %d but got %d", page, expectedSize,
                    pageContent.size());
          }
      );
    });
  }

  @DisplayName("공지사항 상세 정보 조회")
  @Test
  void testFindAnnouncementDetail() {
    // given
    Announcement announcement = announcementJpaRepository.findAll().get(0);
    // when
    Optional<AnnouncementDetail> optionalAnnouncementDetail = announcementQueryRepository.findAnnouncementById(
        announcement.getId());
    // then
    assertThat(optionalAnnouncementDetail)
        .isPresent()
        .hasValueSatisfying(announcementDetail ->
            assertThat(announcementDetail.getId()).isEqualTo(announcement.getId())
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
