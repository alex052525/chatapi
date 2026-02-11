package com.rkd.chatapi.conversation.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class ConversationNotExistException : BusinessException(ErrorCode.CONVERSATION_NOT_FOUND) {
}