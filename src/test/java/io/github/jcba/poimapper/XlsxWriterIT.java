package io.github.jcba.poimapper;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class XlsxWriterIT {

    @Test
    void writesToFile_givenAnnotatedClass() throws IOException {
        var testRows = List.of(
                new ColumnAnnotatedAndFormatted("val1", 1.0111, "test"),
                new ColumnAnnotatedAndFormatted("val4", 2.123113412, "test")
        );

        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("test");
        new XlsxSheetWriter<>(ColumnAnnotatedAndFormatted.class, workbook, sheet).write(testRows.stream());

        var out = new FileOutputStream("/tmp/demo.xlsx");
        workbook.write(out);
        out.close();

        assertThat(Path.of("/tmp/demo.xlsx")).exists();
    }

    record ColumnAnnotatedAndFormatted(
            @Column String first,
            @Column(type = CellType.NUMERIC) @ColumnFormat("#.####") Double second,
            String notWritten
    ) {
    }
}
