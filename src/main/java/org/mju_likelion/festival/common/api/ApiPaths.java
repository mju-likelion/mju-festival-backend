package org.mju_likelion.festival.common.api;

public class ApiPaths {

  // image
  public static final String UPLOAD_IMAGE = "/images";

  // term
  public static final String GET_ALL_TERMS = "/terms";

  // lost item
  private static final String LOST_ITEMS = "/lost-items";
  public static final String GET_ALL_LOST_ITEMS = LOST_ITEMS;
  public static final String SEARCH_LOST_ITEMS = LOST_ITEMS + "/search";
  public static final String POST_LOST_ITEM = LOST_ITEMS;
  public static final String PATCH_LOST_ITEM = LOST_ITEMS + "/{id}";
  public static final String FOUND_LOST_ITEM = LOST_ITEMS + "/{id}/found";
  public static final String DELETE_LOST_ITEM = LOST_ITEMS + "/{id}";

  // booth
  private static final String BOOTHS = "/booths";
  public static final String GET_ALL_BOOTH_DEPARTMENTS = BOOTHS + "/departments";
  public static final String GET_ALL_BOOTHS = BOOTHS;
  public static final String GET_BOOTH = BOOTHS + "/{id}";
  public static final String ISSUE_BOOTH_QR = BOOTHS + "/{id}/qr";
  public static final String VISIT_BOOTH = BOOTHS + "/{qrId}/visit";
  public static final String PATCH_BOOTH = BOOTHS + "/{id}";
  public static final String GET_BOOTH_OWNERSHIP = BOOTHS + "/{id}/ownership";

  // auth
  private static final String AUTH = "/auth";
  public static final String ADMIN_LOGIN = AUTH + "/admin/login";
  public static final String USER_LOGIN = AUTH + "/user/login";
  public static final String AUTH_KEY = AUTH + "/key";

  // announcement
  private static final String ANNOUNCEMENTS = "/announcements";
  public static final String GET_ALL_ANNOUNCEMENTS = ANNOUNCEMENTS;
  public static final String GET_ANNOUNCEMENT = ANNOUNCEMENTS + "/{id}";
  public static final String POST_ANNOUNCEMENT = ANNOUNCEMENTS;
  public static final String PATCH_ANNOUNCEMENT = ANNOUNCEMENTS + "/{id}";
  public static final String DELETE_ANNOUNCEMENT = ANNOUNCEMENTS + "/{id}";
}
