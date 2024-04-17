package ru.fildv.tacocloud.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.fildv.tacocloud.repository.TacoRepository;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TacoCloudInfoContributor implements InfoContributor {
    private final TacoRepository tacoRepository;

    @Override
    public void contribute(final Info.Builder builder) {
        Long tacoCount = tacoRepository.count();
        Map<String, Object> tacoMap = new HashMap<>();
        tacoMap.put("count", tacoCount);
        builder.withDetail("taco-stats", tacoMap);
    }
}
