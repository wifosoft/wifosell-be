package com.wifosell.zeus.database.seeder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoCategoryPayload;
import com.wifosell.zeus.repository.ecom_sync.SendoCategoryRepository;
import com.wifosell.zeus.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class SendoCategorySeeder extends BaseSeeder implements ISeeder {
    private SendoCategoryRepository sendoCategoryRepository;

    @Override
    public void prepareJpaRepository() {
        sendoCategoryRepository = context.getBean(SendoCategoryRepository.class);
    }

    @Override
    public void run() throws FileNotFoundException {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/sendo_categories.json");
            JsonReader reader = new JsonReader(new InputStreamReader(file));

            Gson gson = new Gson();
            ResponseSendoCategoryPayload[] categoryTreePayload = gson.fromJson(reader, ResponseSendoCategoryPayload[].class);
            file.close();

            for (ResponseSendoCategoryPayload categoryPayload : categoryTreePayload) {
                SendoCategory sendoCategory = SendoCategory.builder()
                        .sendoCategoryId(categoryPayload.getId())
                        .var(categoryPayload.isConfigVariant())
                        .name(categoryPayload.getName())
                        .leaf(categoryPayload.isLeaf())
                        .build();

                if (!categoryPayload.isRoot()) {
                    Optional<SendoCategory> parent = sendoCategoryRepository.findBySendoCategoryId(categoryPayload.getParentId());
                    parent.ifPresent(sendoCategory::setParent);
                }

                sendoCategoryRepository.save(sendoCategory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
