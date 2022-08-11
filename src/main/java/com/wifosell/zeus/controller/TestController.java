package com.wifosell.zeus.controller;

import com.google.gson.Gson;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.service.MailService;
import com.wifosell.zeus.service.SendoProductService;
import com.wifosell.zeus.service.impl.batch_process.product.BatchProductExcelService;
import lombok.AllArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("api/test")
@AllArgsConstructor
public class TestController {
    @Autowired
    SendoProductService sendoProductService;

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
        JobId scheduledJobIdEmail = jobScheduler.schedule(Instant.now().plusSeconds(60), () -> mailService.sendEmail("snowdence2911@gmail.com", "Subject", "body"));

        return ResponseEntity.ok(GApiResponse.success("Thêm job thành công", scheduledJobIdEmail.toString()));
    }

    @GetMapping("/testSendoProduct")
    public ResponseEntity<GApiResponse> testSendoProduct(){
        String data = "{\n" +
                "  \"_id\": \"62f2cad763635834aa4c0706\",\n" +
                "  \"created_at\": \"2022-08-10T02:27:35.975Z\",\n" +
                "  \"updated_at\": \"2022-08-10T02:27:35.975Z\",\n" +
                "  \"shop_relation_id\": \"7c18dde1a46746818086310f3779a342\",\n" +
                "  \"id\": 95194406,\n" +
                "  \"name\": \"Quần jean dài Quần jean dài\",\n" +
                "  \"sku\": \"19_95194406\",\n" +
                "  \"price\": 120000,\n" +
                "  \"weight\": 100,\n" +
                "  \"stock_availability\": true,\n" +
                "  \"stock_quantity\": 120,\n" +
                "  \"description\": \"<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\\n<p>Quần jean dài Quần jean dài nè</p>\",\n" +
                "  \"cat_4_id\": 19,\n" +
                "  \"status\": 2,\n" +
                "  \"tags\": null,\n" +
                "  \"updated_date_timestamp\": 1655977326,\n" +
                "  \"created_date_timestamp\": 1655977326,\n" +
                "  \"seo\": \"{\\\"score\\\":\\\"21\\\",\\\"seo_description\\\":\\\"\\\",\\\"seo_keyword\\\":\\\"\\\",\\\"seo_title\\\":\\\"\\\"}\",\n" +
                "  \"link\": \"https://www.sendo.vn/quan-jean-dai-quan-jean-dai-95194406.html\",\n" +
                "  \"relateds\": [\n" +
                "\n" +
                "  ],\n" +
                "  \"seo_keyword\": null,\n" +
                "  \"seo_title\": null,\n" +
                "  \"seo_description\": null,\n" +
                "  \"seo_score\": 21,\n" +
                "  \"image\": \"https://media3.scdn.vn/img4/2022/06_22/OTNt14ZvTtiHAmfNe0Ft.jpg\",\n" +
                "  \"category_4_name\": \"Quần jean nữ\",\n" +
                "  \"promotion_price\": 120000,\n" +
                "  \"brand_id\": 0,\n" +
                "  \"brand_name\": null,\n" +
                "  \"updated_user\": \"2041060041\",\n" +
                "  \"url_path\": \"thoi-trang-nu/quan-nu/quan-jean\",\n" +
                "  \"video_links\": null,\n" +
                "  \"height\": 10,\n" +
                "  \"length\": 10,\n" +
                "  \"width\": 10,\n" +
                "  \"unit_id\": 1,\n" +
                "  \"avatar\": {\n" +
                "    \"picture_url\": \"https://media3.scdn.vn/img4/2022/06_22/OTNt14ZvTtiHAmfNe0Ft.jpg\"\n" +
                "  },\n" +
                "  \"pictures\": [\n" +
                "    {\n" +
                "      \"picture_url\": \"https://media3.scdn.vn/img4/2022/06_22/OTNt14ZvTtiHAmfNe0Ft.jpg\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"certificate_file\": [\n" +
                "\n" +
                "  ],\n" +
                "  \"attributes\": [\n" +
                "    {\n" +
                "      \"attribute_id\": 1490,\n" +
                "      \"attribute_type\": 3,\n" +
                "      \"attribute_name\": \"Họa tiết\",\n" +
                "      \"attribute_is_required\": false,\n" +
                "      \"attribute_code\": \"hoa_tiet\",\n" +
                "      \"attribute_is_custom\": false,\n" +
                "      \"attribute_is_checkout\": false,\n" +
                "      \"attribute_is_image\": false,\n" +
                "      \"attribute_values\": [\n" +
                "        {\n" +
                "          \"id\": 25006,\n" +
                "          \"value\": \"Hoa lá\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25007,\n" +
                "          \"value\": \"Hoạt hình\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25008,\n" +
                "          \"value\": \"Chấm bi\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25009,\n" +
                "          \"value\": \"Caro\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25010,\n" +
                "          \"value\": \"Trơn\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25011,\n" +
                "          \"value\": \"Sọc kẻ\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25012,\n" +
                "          \"value\": \"Ký tự\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25013,\n" +
                "          \"value\": \"Hình động vật\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25014,\n" +
                "          \"value\": \"Họa tiết lạ\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25015,\n" +
                "          \"value\": \"Họa tiết dân gian\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25016,\n" +
                "          \"value\": \"Khối màu - color block\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25017,\n" +
                "          \"value\": \"Boho\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25018,\n" +
                "          \"value\": \"Retro\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25019,\n" +
                "          \"value\": \"Đốm màu\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25020,\n" +
                "          \"value\": \"Rome\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25021,\n" +
                "          \"value\": \"Hình học\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25022,\n" +
                "          \"value\": \"Khác\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25732,\n" +
                "          \"value\": \"Trái cây\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"attribute_id\": 298,\n" +
                "      \"attribute_type\": 2,\n" +
                "      \"attribute_name\": \"Kích thước\",\n" +
                "      \"attribute_is_required\": false,\n" +
                "      \"attribute_code\": \"kich_thuoc_1\",\n" +
                "      \"attribute_is_custom\": false,\n" +
                "      \"attribute_is_checkout\": true,\n" +
                "      \"attribute_is_image\": false,\n" +
                "      \"attribute_values\": [\n" +
                "        {\n" +
                "          \"id\": 25304,\n" +
                "          \"value\": \"27\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14344,\n" +
                "          \"value\": \"8\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 19093,\n" +
                "          \"value\": \"26\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 19095,\n" +
                "          \"value\": \"28\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 19096,\n" +
                "          \"value\": \"29\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 19097,\n" +
                "          \"value\": \"30\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25212,\n" +
                "          \"value\": \"2XS\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25213,\n" +
                "          \"value\": \"7XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25214,\n" +
                "          \"value\": \"8XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25215,\n" +
                "          \"value\": \"US: 00\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25216,\n" +
                "          \"value\": \"US: 0\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25224,\n" +
                "          \"value\": \"US: 16\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25218,\n" +
                "          \"value\": \"US: 4\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25220,\n" +
                "          \"value\": \"US: 8\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25221,\n" +
                "          \"value\": \"US: 10\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25222,\n" +
                "          \"value\": \"US: 12\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25223,\n" +
                "          \"value\": \"US: 14\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25217,\n" +
                "          \"value\": \"US: 2\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25219,\n" +
                "          \"value\": \"US: 6\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 814,\n" +
                "          \"value\": \"Free size\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25225,\n" +
                "          \"value\": \"US: 18\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25226,\n" +
                "          \"value\": \"US: 20\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25227,\n" +
                "          \"value\": \"US: 22\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25228,\n" +
                "          \"value\": \"US: 24\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25229,\n" +
                "          \"value\": \"US: 26\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25230,\n" +
                "          \"value\": \"UK: 2\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25231,\n" +
                "          \"value\": \"UK: 4\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25232,\n" +
                "          \"value\": \"UK: 6\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25233,\n" +
                "          \"value\": \"UK: 8\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25234,\n" +
                "          \"value\": \"UK: 10\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25235,\n" +
                "          \"value\": \"UK: 12\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25236,\n" +
                "          \"value\": \"UK: 14\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25237,\n" +
                "          \"value\": \"UK: 16\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25242,\n" +
                "          \"value\": \"UK: 18\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25243,\n" +
                "          \"value\": \"UK: 20\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25244,\n" +
                "          \"value\": \"UK: 22\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25245,\n" +
                "          \"value\": \"UK: 24\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25246,\n" +
                "          \"value\": \"UK: 26\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25247,\n" +
                "          \"value\": \"UK: 28\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25248,\n" +
                "          \"value\": \"UK: 30\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25249,\n" +
                "          \"value\": \"EU: 30\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25250,\n" +
                "          \"value\": \"EU: 32\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25251,\n" +
                "          \"value\": \"EU: 34\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25252,\n" +
                "          \"value\": \"EU: 36\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25253,\n" +
                "          \"value\": \"EU: 38\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25254,\n" +
                "          \"value\": \"EU: 40\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25255,\n" +
                "          \"value\": \"EU: 42\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25256,\n" +
                "          \"value\": \"EU: 44\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25257,\n" +
                "          \"value\": \"EU: 46\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25258,\n" +
                "          \"value\": \"EU: 48\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25259,\n" +
                "          \"value\": \"EU: 50\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25260,\n" +
                "          \"value\": \"EU: 52\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25261,\n" +
                "          \"value\": \"EU: 54\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25262,\n" +
                "          \"value\": \"EU: 56\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25263,\n" +
                "          \"value\": \"EU: 58\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25264,\n" +
                "          \"value\": \"EU: 60\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25265,\n" +
                "          \"value\": \"AU: 4\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25305,\n" +
                "          \"value\": \"28\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25307,\n" +
                "          \"value\": \"30\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25310,\n" +
                "          \"value\": \"33\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25312,\n" +
                "          \"value\": \"36\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25314,\n" +
                "          \"value\": \"40\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25734,\n" +
                "          \"value\": \"39\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25308,\n" +
                "          \"value\": \"31\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25733,\n" +
                "          \"value\": \"37\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25274,\n" +
                "          \"value\": \"AU: 22\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25275,\n" +
                "          \"value\": \"AU: 24\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25276,\n" +
                "          \"value\": \"AU: 26\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25277,\n" +
                "          \"value\": \"AU: 28\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25278,\n" +
                "          \"value\": \"JP: 5\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25279,\n" +
                "          \"value\": \"JP: 7\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25280,\n" +
                "          \"value\": \"JP: 9\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25281,\n" +
                "          \"value\": \"JP: 11\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25282,\n" +
                "          \"value\": \"JP: 13\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25283,\n" +
                "          \"value\": \"JP: 15\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25284,\n" +
                "          \"value\": \"JP: 17\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25285,\n" +
                "          \"value\": \"JP: 19\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25286,\n" +
                "          \"value\": \"JP: 21\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25287,\n" +
                "          \"value\": \"JP: 23\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25288,\n" +
                "          \"value\": \"JP: 25\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25289,\n" +
                "          \"value\": \"JP: 27\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25290,\n" +
                "          \"value\": \"JP: 29\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25300,\n" +
                "          \"value\": \"23\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25301,\n" +
                "          \"value\": \"24\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25302,\n" +
                "          \"value\": \"25\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25266,\n" +
                "          \"value\": \"AU: 6\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25303,\n" +
                "          \"value\": \"26\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25267,\n" +
                "          \"value\": \"AU: 8\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25268,\n" +
                "          \"value\": \"AU: 10\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25309,\n" +
                "          \"value\": \"32\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25269,\n" +
                "          \"value\": \"AU: 12\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25311,\n" +
                "          \"value\": \"34\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25270,\n" +
                "          \"value\": \"AU: 14\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25313,\n" +
                "          \"value\": \"38\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25735,\n" +
                "          \"value\": \"41\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25736,\n" +
                "          \"value\": \"42\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25737,\n" +
                "          \"value\": \"43\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25738,\n" +
                "          \"value\": \"35\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25273,\n" +
                "          \"value\": \"AU: 20\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25271,\n" +
                "          \"value\": \"AU: 16\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25272,\n" +
                "          \"value\": \"AU: 18\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25306,\n" +
                "          \"value\": \"29\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 820,\n" +
                "          \"value\": \"XS\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 815,\n" +
                "          \"value\": \"S\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 816,\n" +
                "          \"value\": \"M\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 817,\n" +
                "          \"value\": \"L\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 818,\n" +
                "          \"value\": \"XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 18975,\n" +
                "          \"value\": \"2XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 18971,\n" +
                "          \"value\": \"3XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 18972,\n" +
                "          \"value\": \"4XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 18973,\n" +
                "          \"value\": \"5XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 18974,\n" +
                "          \"value\": \"6XL\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14320,\n" +
                "          \"value\": \"1\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14319,\n" +
                "          \"value\": \"2\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14318,\n" +
                "          \"value\": \"3\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14317,\n" +
                "          \"value\": \"4\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14316,\n" +
                "          \"value\": \"5\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14315,\n" +
                "          \"value\": \"6\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14314,\n" +
                "          \"value\": \"7\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 14343,\n" +
                "          \"value\": \"9\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"attribute_id\": 284,\n" +
                "      \"attribute_type\": 1,\n" +
                "      \"attribute_name\": \"Màu sắc\",\n" +
                "      \"attribute_is_required\": false,\n" +
                "      \"attribute_code\": \"mau_sac\",\n" +
                "      \"attribute_is_custom\": false,\n" +
                "      \"attribute_is_checkout\": true,\n" +
                "      \"attribute_is_image\": true,\n" +
                "      \"attribute_values\": [\n" +
                "        {\n" +
                "          \"id\": 602,\n" +
                "          \"value\": \"Nâu\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 603,\n" +
                "          \"value\": \"Vàng\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 604,\n" +
                "          \"value\": \"Trắng\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 605,\n" +
                "          \"value\": \"Đen\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 606,\n" +
                "          \"value\": \"Hồng\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 607,\n" +
                "          \"value\": \"Xanh lá\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 608,\n" +
                "          \"value\": \"Xanh nước biển\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 609,\n" +
                "          \"value\": \"Xanh ngọc\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 610,\n" +
                "          \"value\": \"Xanh đen\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 613,\n" +
                "          \"value\": \"Xám\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 614,\n" +
                "          \"value\": \"Tím\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 628,\n" +
                "          \"value\": \"Đỏ\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 629,\n" +
                "          \"value\": \"Cam\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 631,\n" +
                "          \"value\": \"Kem\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 1495,\n" +
                "          \"value\": \"Xanh rêu\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 1497,\n" +
                "          \"value\": \"Họa tiết\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 1476,\n" +
                "          \"value\": \"Khác\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 1577,\n" +
                "          \"value\": \"Bạc\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 15439,\n" +
                "          \"value\": \"Hồng phấn\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"attribute_id\": 1491,\n" +
                "      \"attribute_type\": 3,\n" +
                "      \"attribute_name\": \"Phong cách\",\n" +
                "      \"attribute_is_required\": false,\n" +
                "      \"attribute_code\": \"phong_cach\",\n" +
                "      \"attribute_is_custom\": false,\n" +
                "      \"attribute_is_checkout\": false,\n" +
                "      \"attribute_is_image\": false,\n" +
                "      \"attribute_values\": [\n" +
                "        {\n" +
                "          \"id\": 25023,\n" +
                "          \"value\": \"Dễ thương\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25024,\n" +
                "          \"value\": \"Gợi cảm\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25025,\n" +
                "          \"value\": \"Cổ điển\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25026,\n" +
                "          \"value\": \"Tối giản\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25027,\n" +
                "          \"value\": \"Tự do\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25028,\n" +
                "          \"value\": \"Bohemian\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25029,\n" +
                "          \"value\": \"Thể thao\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25030,\n" +
                "          \"value\": \"Vintage\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25031,\n" +
                "          \"value\": \"Preppy\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25032,\n" +
                "          \"value\": \"Nhẹ nhàng\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25033,\n" +
                "          \"value\": \"Bánh bèo\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25034,\n" +
                "          \"value\": \"Casual\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25035,\n" +
                "          \"value\": \"Công sở\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25036,\n" +
                "          \"value\": \"Năng động\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25037,\n" +
                "          \"value\": \"Lịch sự\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 25038,\n" +
                "          \"value\": \"Khác\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": false,\n" +
                "          \"is_custom\": false\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"attribute_id\": 30121690,\n" +
                "      \"attribute_type\": 3,\n" +
                "      \"attribute_name\": \"Xuất xứ\",\n" +
                "      \"attribute_is_required\": true,\n" +
                "      \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "      \"attribute_is_custom\": true,\n" +
                "      \"attribute_is_checkout\": true,\n" +
                "      \"attribute_is_image\": false,\n" +
                "      \"attribute_values\": [\n" +
                "        {\n" +
                "          \"id\": 109190151,\n" +
                "          \"value\": \"VN\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 109190152,\n" +
                "          \"value\": \"US\",\n" +
                "          \"attribute_img\": null,\n" +
                "          \"is_selected\": true,\n" +
                "          \"is_custom\": true\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"special_price\": 120000,\n" +
                "  \"promotion_from_date_timestamp\": null,\n" +
                "  \"promotion_to_date_timestamp\": null,\n" +
                "  \"is_promotion\": false,\n" +
                "  \"extended_shipping_package\": {\n" +
                "    \"is_using_instant\": false,\n" +
                "    \"is_using_in_day\": false,\n" +
                "    \"is_self_shipping\": false,\n" +
                "    \"is_using_standard\": true,\n" +
                "    \"is_using_eco\": false\n" +
                "  },\n" +
                "  \"variants\": [\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25212\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 602\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190152\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"2XS_Nâu_US\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"602_25212_109190152\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25212\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 602\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190151\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"2XS_Nâu_VN\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"602_25212_109190151\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25212\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 604\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190152\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"2XS_Trắng_US\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"604_25212_109190152\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25212\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 604\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190151\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"2XS_Trắng_VN\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"604_25212_109190151\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25212\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 603\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190152\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"2XS_Vàng_US\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"603_25212_109190152\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25212\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 603\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190151\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"2XS_Vàng_VN\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"603_25212_109190151\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25213\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 602\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190152\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"7XL_Nâu_US\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"602_25213_109190152\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25213\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 602\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190151\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"7XL_Nâu_VN\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"602_25213_109190151\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25213\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 604\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190152\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"7XL_Trắng_US\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"604_25213_109190152\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25213\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 604\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190151\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"7XL_Trắng_VN\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"604_25213_109190151\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25213\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 603\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190152\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"7XL_Vàng_US\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"603_25213_109190152\",\n" +
                "      \"message\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"variant_attributes\": [\n" +
                "        {\n" +
                "          \"attribute_id\": 298,\n" +
                "          \"attribute_code\": \"kich_thuoc_1\",\n" +
                "          \"option_id\": 25213\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 284,\n" +
                "          \"attribute_code\": \"mau_sac\",\n" +
                "          \"option_id\": 603\n" +
                "        },\n" +
                "        {\n" +
                "          \"attribute_id\": 30121690,\n" +
                "          \"attribute_code\": \"attribute_custom_30121690\",\n" +
                "          \"option_id\": 109190151\n" +
                "        }\n" +
                "      ],\n" +
                "      \"variant_is_promotion\": 0,\n" +
                "      \"variant_sku\": \"7XL_Vàng_VN\",\n" +
                "      \"variant_price\": 120000,\n" +
                "      \"variant_special_price\": null,\n" +
                "      \"variant_quantity\": 10,\n" +
                "      \"variant_promotion_start_date_timestamp\": null,\n" +
                "      \"variant_promotion_end_date_timestamp\": null,\n" +
                "      \"variant_is_flash_sales\": null,\n" +
                "      \"variant_campaign_status\": null,\n" +
                "      \"variant_attribute_hash\": \"603_25213_109190151\",\n" +
                "      \"message\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"is_config_variant\": true,\n" +
                "  \"is_invalid_variant\": false,\n" +
                "  \"voucher\": {\n" +
                "    \"product_type\": 1,\n" +
                "    \"start_date\": null,\n" +
                "    \"end_date\": null,\n" +
                "    \"is_check_date\": null\n" +
                "  },\n" +
                "  \"product_category_types\": [\n" +
                "    1\n" +
                "  ],\n" +
                "  \"is_flash_sales\": null,\n" +
                "  \"campaign_status\": null,\n" +
                "  \"can_edit\": true,\n" +
                "  \"sendo_video\": [\n" +
                "\n" +
                "  ],\n" +
                "  \"installments\": [\n" +
                "\n" +
                "  ]\n" +
                "}";

        var responseModel = (new Gson()).fromJson(data, ResponseSendoProductItemPayload.class);
        if (responseModel != null) {
            sendoProductService.consumeSingleSendoProductFromAPI(responseModel);
        }

        return ResponseEntity.ok(GApiResponse.success(""));
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

