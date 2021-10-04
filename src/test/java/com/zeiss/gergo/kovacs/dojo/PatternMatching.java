package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PatternMatching implements BasicFunctionalities {

    interface DataSource {
        byte[] getData();
    }

    record Data(byte[] data) implements DataSource {
        @Override
        public byte[] getData() {
            return data;
        }
    }

    static class CompressedData implements DataSource {
        private final byte[] data;

        public CompressedData(byte[] data) {
            this.data = compress(data);
        }

        private byte[] compress(byte[] data) {
            // ... compressing data ... //
            return data;
        }

        private byte[] decompress(byte[] data) {
            // ... compressing data ... //
            return data;
        }

        public byte[] getDecompressedData() {
            return decompress(data);
        }

        @Override
        public byte[] getData() {
            return data;
        }
    }

    static class EncryptedData implements DataSource {
        private final byte[] data;

        public EncryptedData(byte[] data, String key) {
            this.data = encrypt(data, key);
        }

        private byte[] encrypt(byte[] data, String key) {
            // ... encrypting data ... //
            return data;
        }

        private byte[] decrypt(byte[] data, String key) {
            // ... decrypting data ... //
            return data;
        }

        public byte[] getDecryptedData(String key) {
            return decrypt(data, key);
        }

        @Override
        public byte[] getData() {
            return data;
        }
    }

    private final String key = "x#210wa--d12..";

    private byte[] extractData(final DataSource data) {
        if (data instanceof CompressedData compressed) {
            return compressed.getDecompressedData();
        }
        if (data instanceof EncryptedData encrypted) {
            return encrypted.getDecryptedData(key);
        }
        return data.getData();
    }

    @Test
    void instanceOf() {
        final var someData = "some data 123".getBytes();
        final var dataList = List.of(
                new Data(someData), new EncryptedData(someData, key), new CompressedData(someData)
        );

        IntStream.rangeClosed(0, 1000)
                 .mapToObj(it -> randomiser.getElement(dataList))
                 .forEach(dataExtractorWithLogger::apply);

    }

    final Function<DataSource, byte[]> dataExtractorWithLogger = (data) -> {
        if (data instanceof CompressedData compressed) {
            logger.print("Decompressing data before returning it");
            return compressed.getDecompressedData();
        }
        if (data instanceof EncryptedData encrypted) {
            logger.print("Decrypting data before returning it");
            return encrypted.getDecryptedData("x#210wa--d12..");
        }
        logger.print("Returning data as is");
        return data.getData();
    };
}
