package org.mju_likelion.festival.booth.service;

import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.booth.util.qr.BoothQrStrategy;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManagerContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothServiceUtil {

  private final BoothQrManagerContext boothQrManagerContext;

  public BoothQrManager boothQrManager(final BoothQrStrategy boothQrStrategy) {
    return boothQrManagerContext.boothQrManager(boothQrStrategy);
  }

  public BoothQrManager boothQrManager() {
    return boothQrManagerContext.boothQrManager();
  }
}
