package com.weit2nd.domain.model.review

enum class PostReviewVerificationState {
    INVALID_IMAGE,
    EMPTY_CONTENTS,
    TOO_MANY_CONTENTS,
    INVALID_RATING,
    VALID,
}
