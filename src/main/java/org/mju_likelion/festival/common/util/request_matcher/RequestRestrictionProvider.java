package org.mju_likelion.festival.common.util.request_matcher;

import java.util.List;

public interface RequestRestrictionProvider {

  List<RequestMatcher> getNotAllowedRequestMatchers();
}
