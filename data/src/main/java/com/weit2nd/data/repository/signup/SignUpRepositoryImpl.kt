package com.weit2nd.data.repository.signup

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.user.SignUpRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.signup.SignUpDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.exception.user.SignUpException
import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.repository.signup.SignUpRepository
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_CONFLICT
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import retrofit2.HttpException
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: SignUpDataSource,
    private val localImageDatasource: LocalImageDatasource,
    private val moshi: Moshi,
) : SignUpRepository {

    private val nicknameCondition = '가'..'힣'

    override suspend fun registerUser(
        image: String,
        nickname: String,
        agreedTermIds: List<Long>,
    ) {
        val imagePart = localImageDatasource.getImageMultipartBodyPart(
            uri = image,
            formDataName = "profileImage",
            imageName = System.currentTimeMillis().toString(),
        )
        val request = SignUpRequest(
            nickname = nickname,
            agreedTermIds = agreedTermIds,
        )
        val signUpPart = moshi.adapter(SignUpRequest::class.java).getMultiPart(
            formDataName = "signUpRequest",
            fileName = "signUpRequest",
            request = request,
        )
        runCatching {
            signUpDataSource.signUp(
                image = imagePart,
                signUpRequest = signUpPart,
            )
        }.onFailure {
            throwSignUpException(it)
        }
    }

    private fun throwSignUpException(throwable: Throwable) {
        val message = throwable.message.toString()
        val exception = if (throwable is HttpException) {
            when (throwable.code()) {
                HTTP_BAD_REQUEST -> SignUpException.BadRequestException(message)
                HTTP_UNAUTHORIZED -> SignUpException.InvalidTokenException(message)
                HTTP_CONFLICT -> SignUpException.DuplicateUserException(message)
                else -> throwable
            }
        } else {
            throwable
        }
        throw exception
    }

    override fun verifyNickname(nickname: String): NicknameState {
        return when {
            nickname.isEmpty() -> NicknameState.EMPTY
            nickname.any { it.isWhitespace() } -> NicknameState.INVALID_CONTAIN_SPACE
            nickname.any {
                it.isLetterOrDigit().not() && it !in nicknameCondition
            } -> NicknameState.INVALID_CHARACTERS
            (nickname.length in 6..16).not() -> NicknameState.INVALID_LENGTH
            else -> NicknameState.VALID
        }
    }

    override suspend fun checkNicknameValidation(nickname: String): NicknameState {
        val nicknameState = verifyNickname(nickname)
        if (nicknameState != NicknameState.VALID) {
            return nicknameState
        }

        return if (signUpDataSource.checkNicknameDuplication(nickname).isDuplicated) {
            NicknameState.DUPLICATE
        } else {
            NicknameState.CAN_SIGN_UP
        }
    }
}
