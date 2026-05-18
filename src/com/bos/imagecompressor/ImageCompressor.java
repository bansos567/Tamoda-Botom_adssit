
package com.bos.imagecompressor;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;

@DesignerComponent(version = 1,
    description = "Extension Compressor + Resizer Gambar Canggih Lipatan Bos!",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class ImageCompressor extends AndroidNonvisibleComponent {

    private Context context;

    public ImageCompressor(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    @SimpleFunction(description = "Kompres dan Resize gambar super canggih bos. Atur lebar maksimal (maxWidth) dan kualitas (1-100) biar ukuran drop tanpa pecah.")
    public String CompressAndResizeImage(String inputPath, String outputPath, int maxWidth, int quality) {
        try {
            // 1. Load file gambar asli ke bitmap Android
            Bitmap bitmap = BitmapFactory.decodeFile(inputPath);
            if (bitmap == null) {
                return "Error: Gambar gagal di-load bos. Periksa path filenya.";
            }

            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();
            Bitmap finalBitmap = bitmap;

            // 2. Fitur Resizer Otomatis jika melebihi batas maxWidth (menjaga ketajaman)
            if (originalWidth > maxWidth) {
                float ratio = (float) originalWidth / (float) maxWidth;
                int newHeight = (int) (originalHeight / ratio);
                // createScaledBitmap dengan filter 'true' agar hasil scaling tetap halus/anti-aliasing
                finalBitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true);
            }

            // 3. Siapkan output file target
            File file = new File(outputPath);
            // Buat folder induk jika belum ada
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fOut = new FileOutputStream(file);

            // 4. Proses kompresi kualitas gambar (Konversi murni ke JPEG)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
            
            // Flush dan close stream untuk mengamankan file
            fOut.flush();
            fOut.close();

            // 5. Kembalikan jalur file absolut yang sukses dibuat
            return file.getAbsolutePath();
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
