package com.csvfv.batch;

import com.csvfv.util.MessageUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CSVService {
    public static ArrayList<String> getFile(String csvFile, String compCode) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(csvFile)));
            String line = "";
            String result = "";
            String[] tempArr = {};
            while ((line = br.readLine()) != null) {
                if (csvFile.contains("REAFILE")) {
                    result = CheckGroup(tempArr,55,compCode, line,"realisasi");
                } else if (csvFile.contains("PENGURUS")) {
                    result = CheckGroup(tempArr,21,compCode, line,"pengurus");
                } else if (csvFile.contains("REPAYMENT")){
                    result = CheckGroup(tempArr,7,compCode, line,"repayment");
                }
                arrayList.add(result);
            }
            br.close();
        }
        catch(IOException ioe) {
            Connec.info("file not found or file different date");
        }
        return arrayList;
    }
    public static void writeFile(String pathfile, String arg, String arg1, String exportFile) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")).toString();
        String path  = arg + "_" + arg1 + "_" + today + ".csv";
        String impor = pathfile + path;
        ArrayList<String> list = getFile(impor, arg);
        try {
            FileWriter succesFileWriter = new FileWriter("result/Laporan_Berhasil_" + exportFile + path);
            FileWriter failedFileWriter = new FileWriter("result/Laporan_Gagal_" + exportFile + path);
            for (String s : list) {
                if (s.contains("OK")) {
                    succesFileWriter.write(s + System.lineSeparator());
                } else {
                    failedFileWriter.write(s + System.lineSeparator());
                }
            }
//            Connec.info(impor);
            succesFileWriter.close();
            failedFileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String CheckGroup(String[] tempArr1, int i, String compCode, String line, String filename) {
        String[] tempArr = tempArr1;
        String keterangan = "";
        String result = "";
        tempArr = line.split("\\|", i);
        result = tempArr[0] + "|";
        if (Check(tempArr, compCode, filename).size() == 0) {
            result += "OK";
            keterangan = result;
        } else {
            for (String s : Check(tempArr, compCode, filename)) {
                result += s + "#";
            }
            keterangan = result;
        }

        switch (filename) {
            case "realisasi":
//                System.out.println(Arrays.toString(tempArr));
                Connec.addData("OS_REAFILE", tempArr, keterangan);
                Connec.info(filename);
                break;
            case "pengurus":
//                System.out.println(Arrays.toString(tempArr));
                Connec.addData("OS_PENGURUS", tempArr, keterangan);
                Connec.info(filename);
                break;
            case "repayment":
//                System.out.println(Arrays.toString(tempArr));
                Connec.addData("OS_REPAYMENT", tempArr, keterangan);
                Connec.info(filename);
                break;
        }
        return result;
    }

    private static ArrayList<String> Check(String[] tempArr, String compCode, String nameFile) {
        ArrayList<String> listCheck = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        switch (nameFile) {
            case "realisasi":
                listCheck.add(checkValidationNomorAplikasi(tempArr[0], "Nomor Aplikasi", compCode));
                listCheck.add(checkValidationNamaDebitur(tempArr[1], "Nama Debitur"));
                listCheck.add(checkvalidationJenisDebitur(tempArr[2], "Jenis Debitur"));
                listCheck.add(checkvalidationGenderCode(tempArr[2], tempArr[3], "Gender"));
                listCheck.add(checkvalidationNIK(tempArr[2], tempArr[3], tempArr[4], "NIK"));
                listCheck.add(checkvalidationNomorNPWP(tempArr[2], tempArr[5], "Nomor NPWP"));
                listCheck.add(checkvalidationAlamat(tempArr[6], "Alamat"));
                listCheck.add(checkvalidationAlamat(tempArr[7], "Alamat Kelurahan"));
                listCheck.add(checkvalidationAlamat(tempArr[8], "Alamat Kecamatan"));
                listCheck.add(checkvalidationAlamatKodePos(tempArr[9], "Alamat KodePos"));
                listCheck.add(checkvalidationKodeDati(tempArr[10], "Alamat KodeDATI II"));
                listCheck.add(checkvalidationAlamat(tempArr[11], "Province"));
                listCheck.add(checkvalidationTelepon(tempArr[12], "No Telepon"));
                listCheck.add(checkvalidationMobilePhone(tempArr[2], tempArr[13], "Mobile Phone Number"));
                listCheck.add(checkvalidationEmail(tempArr[14], "Email"));
                listCheck.add(checkvalidationPlace(tempArr[2], tempArr[15], "Place Of Birth"));
                listCheck.add(checkvalidationTanggalLahirDebitur(tempArr[2], tempArr[16], "Tanggal lahir debitur"));
                listCheck.add(checkvalidationLastEducationCode(tempArr[17], "Last Education Code"));
                listCheck.add(checkvalidationEmployer(tempArr[2], tempArr[18], "Employer"));
                listCheck.add(checkvalidationEmployer(tempArr[2], tempArr[19], "Employer Address"));
                listCheck.add(checkvalidationMotherMaidenName(tempArr[2], tempArr[20], "Mother Maiden Name"));
                listCheck.add(checkvalidationReligion(tempArr[2], tempArr[21], "Religion"));
                listCheck.add(checkvalidationPenghasilanKotorPerBulan(tempArr, "Penghasilan Kotor per Bulan"));
                listCheck.add(checkvalidationMaritalStatus(tempArr[2], tempArr[23], "Marital Status"));
                listCheck.add(checkvalidationNamaPasangan(tempArr[24], "Nama Pasangan"));
                listCheck.add(checkvalidationTanggalLahirPasangan(tempArr[25], "Tanggal Lahir Pasangan"));
                listCheck.add(checkvalidationPerjanjianPisahHarta(tempArr[26], "Perjanjian Pisah Harta"));
                listCheck.add(checkvalidationNoAkte(tempArr[2], tempArr[27], "No Akte"));
                listCheck.add(checkvalidationTanggalBerdiri(tempArr[2], tempArr[28], "Tanggal Berdiri"));
                listCheck.add(checkvalidationNoAkteTerakhir(tempArr[2], tempArr[29], "No Akte Terakhir"));
                listCheck.add(checkvalidationTanggalAkteTerahir(tempArr[2], tempArr[30], "Tanggal Akte Terakhir"));
                listCheck.add(checkvalidationBidangUsaha(tempArr[2], tempArr[31], "Bidang Usaha"));
                listCheck.add(checkvalidationJangkaWaktu(tempArr, "Jangka Waktu"));
                listCheck.add(checkvalidationJenisKredit(tempArr, "Jenis Kredit"));
                listCheck.add(checkvalidationPlafon(tempArr, "Plafon"));
                listCheck.add(checkvalidationInterestRate(tempArr, "Interest Rate"));
                listCheck.add(checkvalidationNomorPK(tempArr[36], "Nomor PK"));
                listCheck.add(checkvalidationTanggalAkad(tempArr[37], "Tanggal Akad"));
                listCheck.add(checkvalidationTanggalAngsuranI(tempArr[38], "Tanggal Angsuran I"));
                listCheck.add(checkvalidationJenisPenggunaan(tempArr[39], "Jenis Penggunaan"));
                listCheck.add(checkvalidationSektorEkonomi(tempArr[2], tempArr[40], "Sektor Ekonomi"));
                listCheck.add(checkvalidationOmzet(tempArr, "Omzet"));
                listCheck.add(checkvalidationGoPublic(tempArr[2], tempArr[42], "Go Public"));
                listCheck.add(checkvalidationSandiGolonganDebitur(tempArr[2], tempArr[43], "Sandi Golongan Debitur"));
                listCheck.add(checkvalidationPenghasilanKotorPerTahun(tempArr, "Penghasilan Kotor per Tahun"));
                listCheck.add(checkvalidationBentukBadanUsaha(tempArr[2], tempArr[45], "Bentuk Badan Usaha"));
                listCheck.add(checkvalidationTempatBerdiriBadanUsaha(tempArr[2], tempArr[46], "Tempat berdiri badan usaha"));
                listCheck.add(checkvalidationOriginalLoanAmount(tempArr, "Original Loan Amount"));
                listCheck.add(checkvalidationDisbursementDate(tempArr[48], "Disbursement Date"));
                listCheck.add(checkvalidationTenor(tempArr[49]));
                listCheck.add(checkvalidationSegmentasiDebitur(tempArr[50], "Segmentasi Debitur"));
                listCheck.add(checkvalidationKodePekerjaan(tempArr[2], tempArr[51], "Kode Pekerjaan"));
                listCheck.add(checkvalidationDebtorCategory(tempArr[52], "Debtor Category"));
                listCheck.add(checkvalidationIncomeSource(tempArr[2], tempArr[53], "Income Source"));
                listCheck.add(checkvalidationJumlahTanggungan(tempArr, "Jumlah Tanggungan"));
                Connec.info("listCheckRealisasi");
                break;
            case "pengurus":
                listCheck.add(checkValidationNomorAplikasi(tempArr[0], "Nomor Aplikasi", compCode));
                listCheck.add(checkValidationNomorUrutPengurus(tempArr, "Nomor Urut Pengurus"));
                listCheck.add(checkValidationJumlahPengurus(tempArr, "Jumlah Pengurus"));
                listCheck.add(checkValidationSandiJabatanBI(tempArr[3], "Sandi Jabatan BI"));
                listCheck.add(checkValidationPangsaKepemilikan(tempArr, "Pangsa Kepemilikan"));
                listCheck.add(checkValidationBentukPengurus(tempArr[5], "Bentuk Pengurus"));
                listCheck.add(checkValidationModaldasar(tempArr, "Modal dasar"));
                listCheck.add(checkValidationModaldisetor(tempArr, "Modal disetor"));
                listCheck.add(checkValidationModalditempatkan(tempArr, "Modal ditempatkan"));
                listCheck.add(checkValidationNPWPPengurus(tempArr[5], tempArr[9], "NPWP Pengurus"));
                listCheck.add(checkValidationNamaPengurus(tempArr[10], "Nama Pengurus"));
                listCheck.add(checkValidationAlamatPengurus(tempArr[11], "Alamat Pengurus"));
                listCheck.add(checkValidationAlamat_Kelurahan(tempArr[12], "Alamat_Kelurahan"));
                listCheck.add(checkValidationAlamat_Kecamatan(tempArr[13], "Alamat_Kecamatan"));
                listCheck.add(checkValidationAlamat_Dati_2(tempArr[14], "Alamat_Dati II"));
                listCheck.add(checkValidationNo_KTP(tempArr[20], tempArr[5], tempArr[15], "No KTP"));
                listCheck.add(checkValidationNo_Akte(tempArr[5], tempArr[16], "No Akte"));
                listCheck.add(checkValidationTanggal_Lahir(tempArr[5], tempArr[17], "Tanggal Lahir"));
                listCheck.add(checkValidationTanggal_Akte(tempArr[5], tempArr[18], "Tanggal Akte"));
                listCheck.add(checkValidationDati_II_tempat_lahir(tempArr[5], tempArr[19], "Dati II tempat lahir"));
                listCheck.add(checkValidationJenis_Kelamin_Pengurus(tempArr[20], "Jenis Kelamin Pengurus"));
                Connec.info("listCheckPengurus");
                break;
            case "repayment":
                listCheck.add(checkValidationNomorAplikasi(tempArr[0], "Nomor Aplikasi", compCode));
                listCheck.add(checkValidationNomorPembayaran(tempArr[1], "Nomor Pembayaran"));
                listCheck.add(checkValidationTanggalPembayaran(tempArr[2], "Tanggal Pembayaran"));
                listCheck.add(checkValidationPelunasan(tempArr[6]));
                listCheck.addAll(checkValidationNominal(tempArr));
                Connec.info("listCheckRepayment");
                break;
        }
        for (String s : listCheck) {
            if (!s.equals("VALID")) {
                result.add(s);
            }
        }
        return result;
    }

    private static String checkvalidationAlamatKodePos(String s, String alamat_kodePos) {
        if (s.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat_kodePos);
        } else if (s.length() != 5 || s.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(alamat_kodePos);
        }
        return "VALID";

    }

    public static ArrayList<String> checkValidationNominal(String[] tempArr) {
        ArrayList<String> result = new ArrayList<>();
        if (tempArr[3].matches("000000000000000|0") &&
                tempArr[4].matches("000000000000000|0") &&
                tempArr[5].matches("000000000000000|0")) {
            result.add(MessageUtil.ErrorWithMessage("Nominal Pokok, Bunga, Denda" , "tidak boleh 0 semua"));
        }
        if (tempArr[3].isEmpty()) {
            result.add(MessageUtil.NotEmptyMessage("Nominal Pokok"));
        } else if (tempArr[3].length() != 15 || !tempArr[3].matches("^[0-9]+$")) {
            result.add(MessageUtil.ErrorNotValid("Nominal Pokok"));
        } else {
            result.add("VALID");
        }
        if (tempArr[4].isEmpty()) {
            result.add(MessageUtil.NotEmptyMessage("Nominal Denda"));
        } else if (tempArr[4].length() != 15 || !tempArr[4].matches("^[0-9]+$")) {
            result.add(MessageUtil.ErrorNotValid("Nominal Bunga"));
        } else {
            result.add("VALID");
        }
        if (tempArr[5].isEmpty()) {
                result.add(MessageUtil.NotEmptyMessage("Nominal Pokok"));
        } else if (tempArr[3].length() != 15 || !tempArr[3].matches("^[0-9]+$")) {
            result.add(MessageUtil.ErrorNotValid("Nominal Denda"));
        } else {
            result.add("VALID");
        }
        return result;
    }

    public static String checkValidationPelunasan(String pelunasan) {
        if (pelunasan.isEmpty()) {
            return MessageUtil.NotEmptyMessage("pelunasan");
        } else if (pelunasan.length() != 20 || !pelunasan.matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid("pelunasan");
        }
        return pelunasan;
    }

    public static String checkValidationTanggalPembayaran(String tanggalPembayaran, String tanggal_pembayaran) {
        if (!tanggalPembayaran.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
            return MessageUtil.ErrorWithMessage("Format" + tanggal_pembayaran, "Pembayaran Harus DDMMYYYY");
        }
        return "VALID";
    }

    public static String checkValidationNomorPembayaran(String nomorPembayaran, String nomor_pembayaran) {
        if (nomorPembayaran.isEmpty()) {
            return MessageUtil.NotEmptyMessage(nomor_pembayaran);
        } else if (nomorPembayaran.length() != 20 || !nomorPembayaran.matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(nomor_pembayaran);
        }
        return nomorPembayaran;
    }

    public static String checkValidationJenis_Kelamin_Pengurus(String jenisKelamin, String jenis_kelamin_pengurus) {
        if (jenisKelamin.isEmpty()) {
            return MessageUtil.NotEmptyMessage(jenis_kelamin_pengurus);
        } else if  (!jenisKelamin.matches("L|P|B|M")) {
            return MessageUtil.ErrorNotValid(jenis_kelamin_pengurus);
        }
        return "VALID";
    }

    public static String checkValidationDati_II_tempat_lahir(String bentukPengurus, String dati2, String datiIiTempatLahir) {
        if (bentukPengurus.equals("2")) {
            if (dati2.isEmpty()) {
                return MessageUtil.NotEmptyMessage(datiIiTempatLahir);
            } else if (!Enumuration.dati2(dati2)) {
                return MessageUtil.ErrorNotValid(datiIiTempatLahir);
            }
        }
        return "VALID";
    }

    public static String checkValidationTanggal_Akte(String bentukPengurus, String tanggalAkte, String tanggal_Akte) {
        if (bentukPengurus.equals("1")) {
            if (tanggalAkte.isEmpty()) {
                return MessageUtil.NotEmptyMessage(tanggal_Akte);
            } else if (tanggalAkte.length() != 8 || !tanggalAkte.matches("[0-9]+$")) {
                return MessageUtil.ErrorNotValid(tanggal_Akte);
            } else if (!tanggalAkte.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
                return MessageUtil.ErrorWithMessage("Format " + tanggal_Akte,"Harus DDMMYYYY");

            }
        }
        return "VALID";
    }

    public static String checkValidationTanggal_Lahir(String bentukPengurus, String tanggalLahir, String tanggal_lahir) {
        if (bentukPengurus.equals("2")) {
            if (tanggalLahir.isEmpty()) {
                return MessageUtil.NotEmptyMessage(tanggal_lahir);
            } else if (tanggalLahir.length() != 8 || !tanggalLahir.matches("[0-9]+$")) {
                return MessageUtil.ErrorNotValid(tanggal_lahir);
            } else if (!tanggalLahir.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
                return MessageUtil.ErrorWithMessage("Format " + tanggalLahir,"Harus DDMMYYYY");
            }
        }
        return "VALID";
    }

    public static String checkValidationNo_Akte(String bentukPengurus, String no_akte, String akte) {
        if (bentukPengurus.equals("2")) {
            if (no_akte.isEmpty()) {
                return MessageUtil.NotEmptyMessage(akte);
            } else if (akte.length() > 30) {
                return MessageUtil.ErrorNotValid(akte);
            }
        }
        return "VALID";
    }

    public static String checkValidationNo_KTP(String gender, String bentukPengurus, String noKTP, String no_Ktp) {
        if (bentukPengurus.equals("2")) {
            if (noKTP.isEmpty()) {
                return MessageUtil.NotEmptyMessage(no_Ktp);
            } else if (noKTP.length() != 16 || noKTP.endsWith("0000") ||
                    (Integer.parseInt(noKTP.substring(6,8)) > 31 && gender.equals("L")) ||
                    (Integer.parseInt(noKTP.substring(6,8)) < 31 && gender.equals("P"))) {
                return MessageUtil.ErrorNotValid(no_Ktp);
            }
        }
        return "VALID";
    }

    public static String checkValidationAlamat_Dati_2(String alamatDati, String alamat_dati_ii) {
        if (alamatDati.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat_dati_ii);
        } else if (!Enumuration.dati2(alamatDati)) {
            return MessageUtil.ErrorNotValid(alamat_dati_ii);
        }
        return "VALID";
    }

    public static String checkValidationAlamat_Kecamatan(String alamatKecamatan, String alamat_kecamatan) {
        if (alamatKecamatan.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat_kecamatan);
        } else if (alamatKecamatan.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(alamat_kecamatan);
        }
        return "VALID";
    }

    public static String checkValidationAlamat_Kelurahan(String alamatKelurahan, String alamat_kelurahan) {
        if (alamatKelurahan.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat_kelurahan);
        } else if (alamatKelurahan.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(alamat_kelurahan);
        }
        return "VALID";
    }

    public static String checkValidationAlamatPengurus(String alamatPengurus, String alamat_pengurus) {
        if (alamatPengurus.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat_pengurus);
        } else if (alamatPengurus.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(alamat_pengurus);
        }
        return "VALID";
    }

    public static String checkValidationNamaPengurus(String namaPengurus, String nama_pengurus) {
        if (namaPengurus.isEmpty()) {
            return MessageUtil.NotEmptyMessage(nama_pengurus);
        } else if (namaPengurus.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(nama_pengurus);
        }
        return "VALID";
    }

    public static String checkValidationNPWPPengurus(String bentukPengurus, String npwp, String npwp_pengurus) {
        if (npwp.isEmpty()) {
            return MessageUtil.NotEmptyMessage(npwp_pengurus);
        } else if (!(npwp.length() == 15)) {
            return MessageUtil.ErrorNotValid(npwp_pengurus);
        } else if (!npwp.substring(0,2).matches("01|02|03") && "1".equals(bentukPengurus)) {
            return MessageUtil.ErrorWithMessage(npwp_pengurus, " Untuk Debitur Badan Usaha");
        } else if (npwp.substring(0,2).matches("01|02|03") && "2".equals(bentukPengurus)) {
            return MessageUtil.ErrorWithMessage(npwp_pengurus, " Untuk Debitur Perorangan");
        } else if (npwp.substring(0,2).matches("00")
                && "2".equals(bentukPengurus)) {
            return MessageUtil.ErrorNotValid(npwp_pengurus);
        }
        return "VALID";
    }

    public static String checkValidationModalditempatkan(String[] listPengurus, String modal_ditempatkan) {
        if (listPengurus[8].isEmpty()) {
            return MessageUtil.NotEmptyMessage(modal_ditempatkan);
        } else if (listPengurus[8].length() > 13 || !listPengurus[8].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(modal_ditempatkan);
        }
        return "VALID";
    }

    public static String checkValidationModaldisetor(String[] listPengurus, String modal_disetor) {
        if (listPengurus[7].isEmpty()) {
            return MessageUtil.NotEmptyMessage(modal_disetor);
        } else if (listPengurus[7].length() > 13 || !listPengurus[7].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(modal_disetor);
        }
        return "VALID";
    }

    public static String checkValidationModaldasar(String[] listPengurus, String modal_dasar) {
        if (listPengurus[6].isEmpty()) {
            return MessageUtil.NotEmptyMessage(modal_dasar);
        } else if (listPengurus[6].length() > 13 || !listPengurus[6].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(modal_dasar);
        }
        return "VALID";
    }

    public static String checkValidationBentukPengurus(String bentukPengurus, String bentuk_pengurus) {
        if (bentukPengurus.isEmpty()) {
            return MessageUtil.NotEmptyMessage(bentuk_pengurus);
        } else if (bentukPengurus.length() != 1 || !bentukPengurus.matches("1|2"))  {
            return MessageUtil.ErrorNotValid(bentuk_pengurus);
        }
        return "VALID";
    }

    public static String checkValidationPangsaKepemilikan(String[] listPengurus, String pangsa_kepemilikan) {
        if (listPengurus[4].isEmpty()) {
            return MessageUtil.NotEmptyMessage(pangsa_kepemilikan);
        } else if (listPengurus[4].length() != 5 || !listPengurus[4].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(pangsa_kepemilikan);
        }
        return "VALID";
    }

    public static String checkValidationSandiJabatanBI(String sandi, String sandi_jabatan_bi) {
        if (sandi.isEmpty()) {
            return MessageUtil.NotEmptyMessage(sandi_jabatan_bi);
        } else if (!Enumuration.SandiJabatanBI(sandi)) {
            return MessageUtil.ErrorNotValid(sandi_jabatan_bi);
        }
        return "VALID";
    }

    public static String checkValidationJumlahPengurus(String[] listPengurus, String jumlah_pengurus) {
        if (listPengurus[2].isEmpty()) {
            return MessageUtil.NotEmptyMessage(jumlah_pengurus);
        }else if (!listPengurus[2].matches("^[0-9]+$") || listPengurus[2].length() > 3) {
            return MessageUtil.ErrorNotValid(jumlah_pengurus);
        }
        return "VALID";
    }

    public static String checkValidationNomorUrutPengurus(String[] listPengurus, String nomor_urut_pengurus) {
        if (listPengurus[1].isEmpty()) {
            return MessageUtil.NotEmptyMessage(nomor_urut_pengurus);
        }   else if (!listPengurus[1].matches("^[0-9]+$") || listPengurus[1].length() > 3) {
            return MessageUtil.ErrorNotValid(nomor_urut_pengurus);
        }
        return "VALID";
    }

    public static String checkvalidationJumlahTanggungan(String[] temp, String jumlah_tanggungan) {
        if (!temp[54].isEmpty()) {
            if (!temp[54].matches("^[0-9]+$")) {
                return  MessageUtil.ErrorNotValid(jumlah_tanggungan);
            } else if (Integer.parseInt(temp[54]) < 0) {
                return MessageUtil.ErrorNotValid(jumlah_tanggungan);
            }
        }
        return "VALID";
    }

    public static String checkvalidationIncomeSource(String jenisDebitur, String incomeSource, String income_source) {
        if (jenisDebitur.equals("P")) {
            if (incomeSource.isEmpty()) {
                return MessageUtil.NotEmptyMessage(income_source);
            } else if (!incomeSource.matches("1|2|3")) {
                return MessageUtil.ErrorNotValid(income_source);
            }
        }
        return "VALID";
    }

    public static String checkvalidationDebtorCategory(String debtorCategory, String debtor_category) {
        if (debtorCategory.isEmpty()) {
            return MessageUtil.NotEmptyMessage(debtor_category);
        } else if (debtorCategory.length() != 2 || !debtorCategory.matches("UM|UK|UT|NU")) {
            return  MessageUtil.ErrorNotValid(debtor_category);
        }
        return "VALID";
    }

    public static String checkvalidationKodePekerjaan(String jenisDebitur, String kodePekerjaan, String kode_pekerjaan) {
        if (jenisDebitur.equals("P")) {
            if (kodePekerjaan.isEmpty()) {
                return MessageUtil.NotEmptyMessage(kode_pekerjaan);
            }else if (!Enumuration.KodePekerjaan(kodePekerjaan) || kodePekerjaan.length() != 3) {
                return MessageUtil.ErrorNotValid(kode_pekerjaan);
            }
        }
        return "VALID";
    }

    public static String checkvalidationSegmentasiDebitur(String segmentasiDebitur, String segmentasi_debitur) {
        if (segmentasiDebitur.isEmpty()) {
            return MessageUtil.NotEmptyMessage(segmentasi_debitur);
        } else if (segmentasiDebitur.length() != 2) {
            return MessageUtil.ErrorNotValid(segmentasi_debitur);
        }
        return "VALID";
    }

    public static String checkvalidationTenor(String tenor) {
        if (tenor.isEmpty()) {
            return MessageUtil.NotEmptyMessage("tenor");
        } else if (tenor.length() != 3 || !tenor.matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid("tenor");
        }
        return "VALID";
    }

    public static String checkvalidationDisbursementDate(String date, String disbursement_date) {
        if (date.isEmpty()) {
            return MessageUtil.NotEmptyMessage(disbursement_date);
        } else if (date.length() > 8 || !date.matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(disbursement_date);
        }
        return "VALID";
    }

    public static String checkvalidationOriginalLoanAmount(String[] temp, String original_loan_amount) {
        if (temp[47].isEmpty()) {
            return MessageUtil.NotEmptyMessage(original_loan_amount);
        } else  if (temp[47].length() > 15 || !temp[47].matches("^[0-9]+$")){
            return MessageUtil.ErrorNotValid(original_loan_amount);
        }
        return "VALID";
    }

    public static String checkvalidationTempatBerdiriBadanUsaha(String jenisDebitur, String tempatBerdiriBadanUsaha, String tempat_berdiri_badan_usaha) {
        if (jenisDebitur.equals("B")) {
            if (tempatBerdiriBadanUsaha.isEmpty()) {
                return MessageUtil.NotEmptyMessage(tempat_berdiri_badan_usaha);
            }
        }
        return "VALID";
    }

    public static String checkvalidationBentukBadanUsaha(String jenisDebitur, String bentukBadanUsaha, String bentuk_badan_usaha) {
        if (jenisDebitur.equals("B")) {
            if (bentukBadanUsaha.isEmpty()) {
                return MessageUtil.NotEmptyMessage(bentuk_badan_usaha) + " untuk Debitur Badan";
            } else if (!Enumuration.BentukBadanUsaha(bentukBadanUsaha)) {
                return MessageUtil.ErrorNotValid(bentuk_badan_usaha);
            }
        }
        return "VALID";
    }

    public static String checkvalidationPenghasilanKotorPerTahun(String[] temp, String penghasilan_kotor_per_tahun) {
        if (temp[44].isEmpty()) {
            if (temp[2].equals("P")) {
                return MessageUtil.NotEmptyMessage(penghasilan_kotor_per_tahun);
            } else {
                return "VALID";
            }
        } else  if (temp[44].length() > 17 || !temp[44].matches("^[0-9]+$")){
            return MessageUtil.ErrorNotValid(penghasilan_kotor_per_tahun);
        }
        return "VALID";
    }

    public static String checkvalidationSandiGolonganDebitur(String jenisDebitur, String sandigolDebitur, String sandi_golongan_debitur) {
        if (sandigolDebitur.isEmpty()) {
            return MessageUtil.NotEmptyMessage(sandi_golongan_debitur);
        } else if (!Enumuration.SandiGolonganDebitur(sandigolDebitur)) {
            return MessageUtil.ErrorNotValid(sandi_golongan_debitur);
        }
        return "VALID";
    }

    public static String checkvalidationGoPublic(String jenisDebitur, String goPublic, String go_public) {
        if (jenisDebitur.equals("B")) {
            if (goPublic.isEmpty()) {
                return MessageUtil.NotEmptyMessage(go_public);
            } else if (!goPublic.matches("1|2"))  {
                return MessageUtil.ErrorNotValid(goPublic);
            }
        }
        return "VALID";
    }

    public static String checkvalidationOmzet(String[] temp, String omzet1) {
        if (temp[41].isEmpty()) {
            if (temp[2].equals("B")) {
                return MessageUtil.NotEmptyMessage(omzet1);
            } else {
                return "VALID";
            }
        } else  if (temp[41].length() > 17 || !temp[41].matches("^[0-9]+$")){
            return MessageUtil.ErrorNotValid(omzet1);
        }
        return "VALID";
    }

    public static String checkvalidationSektorEkonomi(String jenisDebitur, String sektorEkonomi, String sektor_ekonomi) {
        if(jenisDebitur.equals("B")) {
            if (sektorEkonomi.isEmpty()) {
                return MessageUtil.NotEmptyMessage(sektor_ekonomi);
            } else if (!Enumuration.SektorEkonomi(sektorEkonomi)) {
                return MessageUtil.ErrorNotValid(sektor_ekonomi);
            }
        }
        return "VALID";
    }

    public static String checkvalidationJenisPenggunaan(String jenisPenggunaan, String jenis_penggunaan) {
        if (jenisPenggunaan.isEmpty()) {
            return MessageUtil.NotEmptyMessage(jenis_penggunaan);
        }
        return "VALID";
    }


    public static String checkvalidationTanggalAngsuranI(String tanggal, String tanggal_angsuran_i) {
        if (tanggal.isEmpty()) {
            return MessageUtil.NotEmptyMessage(tanggal_angsuran_i);
        } else if (tanggal.length() != 8 || !tanggal.matches("^[0-9]+$")
                // not minus 1 and more than 31
                || !tanggal.substring(0,2).matches("^[0-9]+$")
                // not minus and more than 12
                || !tanggal.substring(3,4).matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(tanggal_angsuran_i);
        } else if (!tanggal.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
            return MessageUtil.ErrorWithMessage("Format " + tanggal_angsuran_i,"Harus DDMMYYYY");

        }
        return "VALID";
    }

    public static String checkvalidationTanggalAkad(String tanggal, String tanggal_akad) {
        if (tanggal.isEmpty()) {
            return MessageUtil.NotEmptyMessage(tanggal_akad);
        } else if (tanggal.length() != 8 || !tanggal.matches("^[0-9]+$")
                // not minus 1 and more than 31
                || !tanggal.substring(0,2).matches("^[0-9]+$")
                // not minus and more than 12
                || !tanggal.substring(3,4).matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(tanggal_akad);
        } else if (!tanggal.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
            return MessageUtil.ErrorWithMessage("Format " + tanggal_akad,"Harus DDMMYYYY");
        }
        return "VALID";
    }

    public static String checkvalidationNomorPK(String noPK, String nomor_pk) {
        if (noPK.isEmpty()) {
            return MessageUtil.NotEmptyMessage(nomor_pk);
        }
        return "VALID";
    }

    public static String checkvalidationInterestRate(String[] temp, String interest_rate) {
        if (temp[35].isEmpty()) {
            return MessageUtil.NotEmptyMessage(interest_rate);
        } else if (temp[35].length() != 5 || !temp[35].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(interest_rate);
        }
        return "VALID";
    }

    public static String checkvalidationPlafon(String[] temp, String plafon1) {
        if (temp[34].isEmpty()) {
            return MessageUtil.NotEmptyMessage(plafon1);
        } else if (!temp[34].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(plafon1);
        }
        return "VALID";
    }

    public static String checkvalidationJenisKredit(String[] temp, String jenis_kredit) {
        if (temp[33].isEmpty()) {
            return MessageUtil.NotEmptyMessage(jenis_kredit);
        } else if (!Enumuration.JenisKredit(temp[33]) || temp[33].length() != 3){
            return MessageUtil.ErrorNotValid(jenis_kredit);
        }
        return "VALID";
    }

    public static String checkvalidationJangkaWaktu(String[] temp, String jangka_waktu) {
        if (temp[32].isEmpty()) {
            return MessageUtil.NotEmptyMessage(jangka_waktu);
        } else if (!temp[32].matches("^[0-9]+$") || temp[32].length() != 3){
            return MessageUtil.ErrorNotValid(jangka_waktu);
        }
        return "VALID";
    }

    public static String checkvalidationBidangUsaha(String jenisDebitur, String bidangUsaha, String bidang_usaha) {
        if (jenisDebitur.equals("B")) {
            if (bidangUsaha.isEmpty()) {
                return MessageUtil.NotEmptyMessage(bidang_usaha);
            } else if (!Enumuration.bidangUsaha(bidangUsaha)) {
                return MessageUtil.ErrorNotValid(bidang_usaha);
            }
        }
        return "VALID";
    }

    public static String checkvalidationTanggalAkteTerahir(String jenisDebitur, String tanggal, String tanggal_akte_terakhir) {
            if (tanggal.isEmpty()) {
                if (jenisDebitur.equals("B")) {
                    return MessageUtil.NotEmptyMessage(tanggal_akte_terakhir);
                } else {
                    return "VALID";
                }
            } else if (tanggal.length() != 8 || !tanggal.matches("[0-9]+$")) {
                return MessageUtil.ErrorNotValid(tanggal_akte_terakhir);
            } else if (!tanggal.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
                return MessageUtil.ErrorWithMessage("Format " + tanggal_akte_terakhir,"Harus DDMMYYYY");

            }
        return "VALID";
    }

    public static String checkvalidationNoAkteTerakhir(String jenisDebitur, String noAkteTerakhir, String no_akte_terakhir) {
        if (jenisDebitur.equals("B")) {
            if (noAkteTerakhir.isEmpty()) {
                return MessageUtil.NotEmptyMessage(no_akte_terakhir);
            }
        }
        return "VALID";
    }

    public static String checkvalidationTanggalBerdiri(String jenisDebitur, String tanggal, String tanggal_berdiri) {
        if (jenisDebitur.equals("B")) {
            if (tanggal.isEmpty()) {
                return MessageUtil.NotEmptyMessage(tanggal_berdiri);
            } else if (tanggal.length() != 8 || !tanggal.matches("[0-9]+$")) {
                return MessageUtil.ErrorNotValid(tanggal_berdiri);
            } else if (!tanggal.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
                return MessageUtil.ErrorWithMessage("Format " + tanggal_berdiri,"Harus DDMMYYYY");
            }
        }
        return "VALID";
    }

    public static String checkvalidationNoAkte(String jenisDebitur, String no_akte, String noAkte) {
        if (jenisDebitur.equals("B")) {
            if (no_akte.isEmpty()) {
                return MessageUtil.NotEmptyMessage(noAkte);
            } else if (no_akte.length() > 30) {
                return MessageUtil.ErrorNotValid(noAkte);
            }
        }
        return "VALID";
    }

    public static String checkvalidationPerjanjianPisahHarta(String perjanjianPisahHarta, String perjanjian_pisah_harta) {
        if (perjanjianPisahHarta.isEmpty()) {
            return "VALID";
        } else if (!perjanjianPisahHarta.matches("1|2")) {
            return MessageUtil.ErrorNotValid(perjanjian_pisah_harta);
        }
        return "VALID";
    }

    public static String checkvalidationTanggalLahirPasangan(String tanggalLahir, String tanggal_lahir_pasangan) {
        if (tanggalLahir.isEmpty()) {
            return "VALID";
        } else if (!tanggalLahir.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
            return MessageUtil.ErrorWithMessage("Format " + tanggal_lahir_pasangan,"Harus DDMMYYYY");
        } else if (!tanggalLahir.matches("^[0-9]+$")) {
            return MessageUtil.ErrorWithMessage(tanggal_lahir_pasangan, "Harus Numeric");
        }
        return "VALID";
    }

    public static String checkvalidationNamaPasangan(String nama, String nama_pasangan) {
        if (nama.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(nama_pasangan);
        }
        return "VALID";
    }


    public static String checkvalidationMaritalStatus(String jenisDebitur, String marry, String marital_status) {
        if (jenisDebitur.equals("P")) {
            if (marry.isEmpty()) {
                return MessageUtil.NotEmptyMessage(marital_status);
            } else if (!marry.matches("1|2|3")) {
                return MessageUtil.ErrorNotValid(marital_status);
            }
        }
        return "VALID";
    }

    public static String checkvalidationPenghasilanKotorPerBulan(String[] temp, String penghasilan_kotor_per_bulan) {
        if (temp[22].isEmpty()) {
            if (temp[2].equals("P")) {
                return MessageUtil.NotEmptyMessage(penghasilan_kotor_per_bulan);
            } else {
                return "VALID";
            }
        } else if (!temp[22].matches("^[0-9]+$")) {
            return MessageUtil.ErrorNotValid(penghasilan_kotor_per_bulan);
        }
        return "VALID";
    }

    public static String checkvalidationReligion(String jenisDebitur, String religion, String religion1) {
        if (jenisDebitur.equals("P")) {
            if (religion.isEmpty()) {
                return MessageUtil.NotEmptyMessage(religion1);
            } else if (!religion.matches("1|2|3|4|5|6")) {
                return MessageUtil.ErrorNotValid(religion1);
            }
        }
        return "VALID";
    }

    public static String checkvalidationMotherMaidenName(String jenisDebitur, String mother, String mother_maiden_name) {
        if (jenisDebitur.equals("P")) {
            if (mother.isEmpty()) {
                return MessageUtil.NotEmptyMessage(mother_maiden_name);
            }
        }
        return "VALID";
    }

    public static String checkvalidationEmployer(String jenisDebitur, String employer, String employer1) {
        if (jenisDebitur.equals("P")) {
            if (employer.isEmpty()) {
                return MessageUtil.NotEmptyMessage(employer1);
            }
        }
        return "VALID";
    }

    public static String checkvalidationLastEducationCode(String edu, String last_education_code) {
        if (edu.matches("00|01|02|03|04|05|06|99") || edu.isEmpty()) {
            return "VALID";
        }
        return MessageUtil.ErrorNotValid(last_education_code);
    }

    public static String checkvalidationTanggalLahirDebitur(String jenisDebitur, String tanggalLahir, String tanggal_lahir_debitur) {
        if (jenisDebitur.equals("P")) {
            if (tanggalLahir.isEmpty()) {
                return MessageUtil.NotEmptyMessage(tanggal_lahir_debitur);
            } else if (tanggalLahir.length() > 8 || !tanggalLahir.matches("^[0-9]+$")) {
                return MessageUtil.ErrorNotValid(tanggal_lahir_debitur);
            } else if (!tanggalLahir.matches("^(0[1-9]|[1-2][0-9]|31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([12]\\d{3})$")) {
                return MessageUtil.ErrorWithMessage("Format " + tanggal_lahir_debitur,"Harus DDMMYYYY");
            }
        }
        return "VALID";
    }

    public static String checkvalidationPlace(String jenisDebitur, String place, String place_of_birth) {
        if (jenisDebitur.equals("P") && place.isEmpty()) {
            return MessageUtil.NotEmptyMessage(place_of_birth);
        } else {
            return "VALID";
        }
    }

    // email validation owasp
    public static String checkvalidationEmail(String email, String Email) {
        if (email.isEmpty()) {
            return MessageUtil.NotEmptyMessage(Email);
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            return MessageUtil.ErrorNotValid(Email);
        }
        return "VALID";
    }

    public static String checkvalidationMobilePhone(String jenisDebitur, String mobilePhone, String mobile_phone_number) {
        if (jenisDebitur.equals("P")) {
            if (mobilePhone.isEmpty()) {
                return MessageUtil.NotEmptyMessage(mobile_phone_number);
            } else if (!mobilePhone.matches("^[0-9]+$") ||
                    mobilePhone.length() > 20 || !mobilePhone.substring(0,2).matches("62")) {
                return MessageUtil.ErrorNotValid(mobile_phone_number);
            }
        }
        return "VALID";
    }

    public static String checkvalidationTelepon(String no, String no_telepon) {
        if (no.isEmpty()) {
            return MessageUtil.NotEmptyMessage(no_telepon);
        } else if (!no.matches("^[0-9]+$") || no.length() > 13) {
            return MessageUtil.ErrorNotValid(no_telepon);
        }
        return "VALID";
    }

    public static String checkvalidationKodeDati(String dati2, String alamat_kodeDATI_ii) {
        if (dati2.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat_kodeDATI_ii);
        } else if (!Enumuration.dati2(dati2)) {
            return MessageUtil.ErrorNotValid(alamat_kodeDATI_ii);
        }
        return "VALID";
    }

    public static String checkvalidationAlamat(String alamat, String alamat1) {
        if (alamat.isEmpty()) {
            return MessageUtil.NotEmptyMessage(alamat1);
        } else if (alamat.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(alamat1);
        }
        return "VALID";
    }

    public static String checkvalidationNomorNPWP(String jenisDebitur, String nomor_npwp, String nomorNpwp) {
            //Based on Jenis Debitur
            if (nomor_npwp.isEmpty()) {
                return MessageUtil.NotEmptyMessage(nomorNpwp);
            } else if (!(nomor_npwp.length() == 15)) {
                return MessageUtil.ErrorNotValid(nomorNpwp);
            } else if (nomor_npwp.length() == 15
                    && !"000000000000000"
                    .equals(nomor_npwp)
                    && "P".equals(jenisDebitur)) {
                return MessageUtil.ErrorNotValid(nomorNpwp);
            } else if (!nomor_npwp.substring(0,2).matches("01|02|03") && "B".equals(jenisDebitur)) {
                    return MessageUtil.ErrorNotValid(nomorNpwp);
            } else if (nomor_npwp.substring(0,2).matches("01|02|03") && "P".equals(jenisDebitur)) {
                    return MessageUtil.ErrorNotValid(nomorNpwp);
            } else if (nomor_npwp.substring(0,2).matches("00")
                    && "P".equals(jenisDebitur)) {
                return MessageUtil.ErrorNotValid(nomorNpwp);
            }
            return "VALID";
    }

    public static String checkvalidationNIK(String jenisDebitur, String gender, String nik, String nikcode) {
            if (nik.isEmpty()) {
                if (jenisDebitur.equals("P")) {
                    return MessageUtil.NotEmptyMessage(nikcode + " Untuk Debitur Perorangan");
                } else {
                    return "VALID";
                }
            } else if (nik.length() != 16) {
                return  MessageUtil.ErrorWithMessage(nikcode, "Not 16");
            } else if (nik.endsWith("0000")) {
                return  MessageUtil.ErrorWithMessage(nikcode, "Must not end with 0000");
            } else if ((Integer.parseInt(nik.substring(6,8)) > 31 && gender.equals("L")) ||
                    (Integer.parseInt(nik.substring(6,8)) < 31 && gender.equals("P"))) {
                return  MessageUtil.ErrorNotValid(nikcode);
            }
            return "VALID";
        }
        public static String checkvalidationGenderCode(String s, String gender_code, String genderCode) {
            if (s.equals("P")) {
                if (gender_code.isEmpty()) {
                return MessageUtil.NotEmptyMessage(genderCode);
            } else {
                if (!gender_code.matches("L|P")) {
                    return MessageUtil.ErrorNotValid(genderCode);
                }
            }
        }
        return "VALID";
    }

    public static String checkvalidationJenisDebitur(String s, String jenis_debitur) {
        if (s.isEmpty()) {
            return MessageUtil.NotEmptyMessage(jenis_debitur);
        } else if (!s.matches("P|B")) {
            return MessageUtil.ErrorNotValid(jenis_debitur);
        }
        return "VALID";
    }

    public static String checkValidationNamaDebitur(String s, String nama_debitur) {
        if (s.isEmpty()) {
            return MessageUtil.NotEmptyMessage(nama_debitur);
        } else if (s.matches("^(.*\\s.*){2,}$")) {
            return MessageUtil.ErrorNotValid(nama_debitur);
        } else if (s.length() > 40) {
            return MessageUtil.ErrorNotValid(nama_debitur);
        } else {
            return "VALID";
        }
    }

    public static String checkValidationNomorAplikasi(String nomorAplikasi, String nomor_aplikasi, String arg) {
        if (nomorAplikasi.isEmpty()) {
            return MessageUtil.NotEmptyMessage(nomor_aplikasi);
        } else if (nomorAplikasi.length() != 17 || !nomorAplikasi.substring(0, 7).matches("^[a-zA-Z0-9]+$")) {
            return MessageUtil.ErrorNotValid(nomor_aplikasi);
        } else if (!nomorAplikasi.substring(0,7).equals(arg.substring(0,7))){
            return MessageUtil.ErrorWithMessage(nomor_aplikasi,"Company Code pada File Name berbeda dengan Isi File");
        }
        return "VALID";
    }
    public static void uploadToFileServer(String path,
                                          String fileServerLocation) {
        String originDir = "result/";
        String succesRealisasiFilename = "Laporan_Berhasil_" + path;
        String succesPengurusFilename = "Laporan_Berhasil_" + path;
        String succesRepaymentFilename = "Laporan_Berhasil_" + path;
        String failedRealisasiFilename = "Laporan_Gagal_" + path;
        String failedPengurusFilename = "Laporan_Gagal_" + path;
        String failedRepaymentFilename = "Laporan_Gagal_" + path;

        ArrayList<String> filePath = new ArrayList<String>();
        filePath.add(succesRealisasiFilename);
        filePath.add(failedRealisasiFilename);
        filePath.add(succesPengurusFilename);
        filePath.add(failedPengurusFilename);
        filePath.add(succesRepaymentFilename);
        filePath.add(failedRepaymentFilename);

        for (String fileName : filePath) {
            try {
                Files.move(Paths.get(originDir + fileName), Paths.get(fileServerLocation + fileName), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("UPLOAD FILE " + fileName.replace("result\\", "") + " SUCCEED");
            } catch (IOException e) {
                System.out.println("UPLOAD FAILED");
            }
        }
    }
}
