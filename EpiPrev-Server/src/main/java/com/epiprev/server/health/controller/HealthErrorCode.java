package com.epiprev.server.health.controller;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HealthErrorCode implements ErrorCode {

    TODAY_NOT_RECORD("HEALTH-001", "今日未记录");

    private final String code;

    private final String message;
}
