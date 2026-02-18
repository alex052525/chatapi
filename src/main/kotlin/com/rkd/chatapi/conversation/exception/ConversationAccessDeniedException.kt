package com.rkd.chatapi.conversation.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class ConversationAccessDeniedException : BusinessException(ErrorCode.CONVERSATION_ACCESS_DENIED)
