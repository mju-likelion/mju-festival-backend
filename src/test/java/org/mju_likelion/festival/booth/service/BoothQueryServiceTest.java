package org.mju_likelion.festival.booth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothResponse;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("BoothQueryService")
@ApplicationTest
public class BoothQueryServiceTest {

  @Autowired
  private BoothQueryService boothQueryService;

  @Autowired
  private BoothJpaRepository boothJpaRepository;

  @DisplayName("부스 간단 정보 List 조회")
  @Test
  public void testGetBooths() {
    // given
    int page = 0;
    int size = 5;

    // when
    List<SimpleBoothResponse> booths = boothQueryService.getBooths(page, size);

    // then
    assertThat(booths).isNotEmpty();
  }

  @DisplayName("부스 상세 정보 조회")
  @Test
  public void testGetBooth() {
    // given
    UUID boothIdToFind = boothJpaRepository.findAll().get(0).getId();

    // when
    BoothDetailResponse boothDetailResponse = boothQueryService.getBooth(boothIdToFind);

    // then
    assertThat(boothDetailResponse.getId()).isEqualTo(boothIdToFind);
  }
}
