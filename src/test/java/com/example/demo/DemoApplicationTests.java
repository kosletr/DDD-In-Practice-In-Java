package com.example.demo;

import com.example.demo.snack_machines.SnackMachine;
import com.example.demo.snack_machines.SnackMachineRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@Disabled
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private SnackMachineRepository snackMachineRepository;

	@Test
	void contextLoads() {
		snackMachineRepository.save(new SnackMachine());
	}
}
