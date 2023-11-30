package com.evan.wj.dto.base;

import lombok.NonNull;

import javax.validation.constraints.NotNull;

import static com.evan.wj.util.BeanUtils.updateProperties;

public interface OutputConverter<DTO extends OutputConverter<DTO, DOMAIN>, DOMAIN> {
    @NotNull
    @SuppressWarnings("unchecked")
    default <T extends DTO> T ConvertFrom(@NonNull DOMAIN domain) {
        updateProperties(domain, this);
        return (T) this;
    }
}
