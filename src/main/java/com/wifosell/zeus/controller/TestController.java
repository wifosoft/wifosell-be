package com.wifosell.zeus.controller;

import com.wifosell.zeus.payload.GApiResponse;

import com.wifosell.zeus.service.BatchProductExcelService;
import com.wifosell.zeus.service.MailService;
import com.wifosell.zeus.service.impl.storage.StorageFileNotFoundException;
import lombok.AllArgsConstructor;

import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

import java.time.Instant;
@RestController
@RequestMapping("api/test")
@AllArgsConstructor
public class TestController {
    @Autowired
    private final JobScheduler jobScheduler;

    @Autowired
    private BatchProductExcelService batchProductExcelService;

    @Autowired
    private MailService mailService;



    @GetMapping("/addNewJob")
    public ResponseEntity<GApiResponse<String>> testAddNewJob(@RequestParam("name") String name) {
        //final JobId scheduledJobId = jobScheduler.schedule<BatchProductExcelService>(Instant.now().plusSeconds(30), x -> x.doSimpleJob());
        JobId scheduledJobId = jobScheduler.schedule(Instant.now().plusSeconds(60), () -> batchProductExcelService.doSimpleJob(name));
        JobId scheduledJobIdEmail = jobScheduler.schedule(Instant.now().plusSeconds(60), () -> mailService.sendEmail("snowdence2911@gmail.com" , "Subject" , "body"));

        return ResponseEntity.ok(GApiResponse.success("Thêm job thành công" , scheduledJobIdEmail.toString()));
    }

//
//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//
//        model.addAttribute("files", storageService.loadAll().map(
//                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                                "serveFile", path.getFileName().toString()).build().toUri().toString())
//                .collect(Collectors.toList()));
//
//        return "uploadForm";
//    }

}

