package com.example.tiktok_clone.core.navigation

object NavigationRoutes {
    const val mainWrapper = "main_wrapper"


    /** Nested graph: shared SearchViewModel between home + result */
    const val searchGraphRoute = "search_flow"
    const val searchHomeRoute = "search_flow/home"
    const val searchResultRoute = "search_flow/result"


    const val selectSignUpRoute = "select_signup"
    const val loginSelectionRoute = "login_selection"
    const val loginFormRoute = "login_form"
    const val signUpFormRoute = "signup_form"


    const val PREVIEW_SCREEN_ROUTE = "preview_screen/{mediaUri}/{postType}"
    const val cameraAccessRoute = "camera_access"
    const val prePostScreenRoute = "pre_post_screen/{mediaUri}/{postType}"
    const val inboxRoute = "inbox"
}