package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothResponse;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BoothQueryService {

  private final BoothQueryRepository boothQueryRepository;

  public List<SimpleBoothResponse> getBooths(final int page, final int size) {
    return boothQueryRepository.findOrderedSimpleBoothsWithPagination(page, size)
        .stream().map(SimpleBoothResponse::from).toList();
  }

  public BoothDetailResponse getBooth(final UUID id) {
    return BoothDetailResponse.from(
        boothQueryRepository.findBoothById(id).orElseThrow(
            () -> new NotFoundException(BOOTH_NOT_FOUND_ERROR)
        )
    );
  }
}

