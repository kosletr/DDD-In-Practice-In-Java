package com.example.demo.logic.utils;


import com.example.demo.logic.common.DomainEvents;
import com.example.demo.logic.management.BalanceChangedEventHandler;
import com.example.demo.logic.management.HeadOfficeInstance;
import com.example.demo.logic.management.HeadOfficeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Initer implements ApplicationRunner {
	private final HeadOfficeRepository headOfficeRepository;

	public Initer(HeadOfficeRepository headOfficeRepository) {
		this.headOfficeRepository = headOfficeRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		HeadOfficeInstance.init(headOfficeRepository);
		DomainEvents.init(List.of(
				new BalanceChangedEventHandler(headOfficeRepository)
		));
	}
}