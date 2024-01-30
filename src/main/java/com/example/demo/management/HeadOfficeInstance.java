package com.example.demo.management;

public final class HeadOfficeInstance {
    private static HeadOffice instance;

    private static final long HEAD_OFFICE_ID = 1L;

    private HeadOfficeInstance() {
    }

    public static void init(HeadOfficeRepository headOfficeRepository) {
        if (instance == null)
            instance = headOfficeRepository.findById(HEAD_OFFICE_ID).orElseThrow();
    }

    public static HeadOffice getInstance() {
        return instance;
    }
}
