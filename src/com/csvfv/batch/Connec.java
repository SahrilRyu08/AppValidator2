package com.csvfv.batch;

import org.apache.log4j.Logger;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Connec {
    private static Logger bLog = null;
    public static void addfield(String table) {
        try(Connection conn = DatabaseHelper.getInstance().getConnection();
            Statement stmt = conn.createStatement();) {
            switch (table) {
                case "OS_REAFILE": {
                    String sqlRealisasi = "CREATE TABLE IF NOT EXISTS " + table +
                            "(id integer  NOT NULL,created_by integer  NOT NULL,updated_by integer  NOT NULL,created_date date  NOT NULL,updated_date date  NOT NULL,status VARCHAR (3) NOT NULL,respon_status VARCHAR (3) ,keterangan_gagal VARCHAR (255) ,no_aplikasi VARCHAR (51) NOT NULL,nama_debitur VARCHAR (80) NOT NULL,jenis_debitur VARCHAR (4) NOT NULL,gender_cd VARCHAR (4) ,nik VARCHAR (48) ,npwp VARCHAR (45) NOT NULL,alamat VARCHAR (100) NOT NULL,kelurahan VARCHAR (80) NOT NULL,kecamatan VARCHAR (80) NOT NULL,kodepos VARCHAR (20) NOT NULL,dati2 VARCHAR (16) NOT NULL,provinsi VARCHAR (255) NOT NULL,no_telp VARCHAR (39) NOT NULL,no_hp VARCHAR (40) NOT NULL,email VARCHAR (100) NOT NULL,tempat_lahir VARCHAR (100) ,tgl_lahir date  ,gelar_debitur_cd VARCHAR (8) ,tempat_bekerja VARCHAR (500) ,alamat_tempat_bekerja VARCHAR (500) ,nama_ibu_kandung VARCHAR (100) ,agama_cd VARCHAR (4) ,penghasilan_per_bulan double precision  ,status_perkawinan_cd VARCHAR (4) ,nama_pasangan VARCHAR (80) ,tgl_lahir_pasangan date  ,pisah_harta VARCHAR (4) ,no_akte VARCHAR (60) ,tgl_berdiri date  ,no_akte_terakhir VARCHAR (60) ,tgl_akte_terakhir date  ,bidang_usaha_cd VARCHAR (24) ,jangka_waktu integer  NOT NULL,jenis_kredit_cd VARCHAR (12) NOT NULL,plafon double precision  NOT NULL,suku_bunga double precision  NOT NULL,nomor_pk VARCHAR (60) NOT NULL,tgl_akad date  ,tgl_angsuran_1 date  ,jenis_penggunaan_cd VARCHAR (4) NOT NULL,sektor_ekonomi_cd VARCHAR (24) ,omzet double precision  ,go_public_cd VARCHAR (4) ,golongan_debitur_cd VARCHAR (45) NOT NULL,penghasilan_per_tahun double precision  ,bentuk_badan_cd VARCHAR (8) ,tempat_berdiri_badan VARCHAR (60) ,original_amount double precision  NOT NULL,tgl_disbursement date  ,tenor integer  NOT NULL,segmentasi_debitur_cd VARCHAR (8) NOT NULL,pekerjaan_cd VARCHAR (12) ,debtor_category_cd VARCHAR (8) NOT NULL,sumber_penghasilan_cd VARCHAR (4) ,jumlah_tanggungan integer," +
                            "PRIMARY KEY (id));";
                    stmt.executeUpdate(sqlRealisasi);
                    info("create table realisasi success");
                }
                break;
                case "OS_PENGURUS": {
                    String sqlPengurus = "CREATE TABLE IF NOT EXISTS " + table + "( " +
                            "id integer  NOT NULL,created_by integer  NOT NULL,updated_by integer  NOT NULL,created_date date  NOT NULL,updated_date date  NOT NULL,status VARCHAR (3) NOT NULL,respon_status VARCHAR (3) ,keterangan_gagal VARCHAR (255) ,os_reafile_id integer  NOT NULL,no_aplikasi VARCHAR (51) NOT NULL,no_urut integer  NOT NULL,jumlah_pengurus integer  NOT NULL,jabatan_cd VARCHAR (8) NOT NULL,pangsa_kepemilikan double precision  NOT NULL,bentuk_pengurus VARCHAR (4) NOT NULL,modal_dasar double precision  NOT NULL,modal_disetor double precision  NOT NULL,modal_ditempatkan double precision  NOT NULL,npwp VARCHAR (45) NOT NULL,nama VARCHAR (80) NOT NULL,alamat VARCHAR (80) NOT NULL,kelurahan VARCHAR (80) NOT NULL,kecamatan VARCHAR (80) NOT NULL,dati2 VARCHAR (16) NOT NULL,no_ktp VARCHAR (48) ,no_akte VARCHAR (60) ,tgl_lahir date  ,tgl_akte date  ,dati2_tempat_lahir VARCHAR (16) ,gender_pengurus_cd VARCHAR (4)," +
                            "PRIMARY KEY (id));";
                    stmt.executeUpdate(sqlPengurus);
                    info("create table pengurus success");
                }
                break;
                case "OS_REPAYMENT": {
                    String sqlRepayment = "CREATE TABLE IF NOT EXISTS " + table +
                            "(id integer NOT NULL," +
                            "created_by integer NOT NULL," +
                            "updated_by integer NOT NULL," +
                            "created_date date NOT NULL," +
                            "updated_date date NOT NULL," +
                            "status VARCHAR (3) NOT NULL," +
                            "respon_status VARCHAR (3)," +
                            "keterangan_gagal VARCHAR (255)," +
                            "nomor_aplikasi VARCHAR NOT NULL," +
                            "nomor_pembayaran VARCHAR (40) NOT NULL," +
                            "tanggal_pambayaran date NOT NULL," +
                            "nominal_pokok integer NOT NULL," +
                            "nominal_bunga integer NOT NULL," +
                            "nominal_denda integer NOT NULL," +
                            "pelunasan VARCHAR (4) NOT NULL," +
                            "PRIMARY KEY (id));";
                    stmt.executeUpdate(sqlRepayment);
                    info("create table repayment success");
                }
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int getIdfromDatabase(String table) {
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement statement = conn.createStatement();) {
                String SQL = "select count(*) as total from " + table + ";";
                ResultSet resultSet = statement.executeQuery(SQL);
                while (resultSet.next()) {
                    return resultSet.getInt("total");
                }
                resultSet.close();
        } catch (Exception exception) {
            System.out.println("wrong");
        }
        return 0;
    }

    public static void addData(String table, String[] list, String keterangan) {
        int id = getIdfromDatabase(table);
        switch (table) {
            case "OS_REAFILE" : {
                String sqlRealisasi = "INSERT INTO " + table + "(" +
                        "id," +
                        "created_by," +
                        "updated_by," +
                        "created_date," +
                        "updated_date," +
                        "status," +
                        "respon_status,keterangan_gagal,no_aplikasi,nama_debitur,jenis_debitur,gender_cd,nik,npwp,alamat,kelurahan,kecamatan,kodepos,dati2,provinsi,no_telp,no_hp,email,tempat_lahir,tgl_lahir,gelar_debitur_cd,tempat_bekerja,alamat_tempat_bekerja,nama_ibu_kandung,agama_cd,penghasilan_per_bulan,status_perkawinan_cd,nama_pasangan,tgl_lahir_pasangan,pisah_harta,no_akte,tgl_berdiri,no_akte_terakhir,tgl_akte_terakhir,bidang_usaha_cd,jangka_waktu,jenis_kredit_cd,plafon,suku_bunga,nomor_pk,tgl_akad,tgl_angsuran_1,jenis_penggunaan_cd,sektor_ekonomi_cd,omzet,go_public_cd,golongan_debitur_cd,penghasilan_per_tahun,bentuk_badan_cd,tempat_berdiri_badan,original_amount,tgl_disbursement,tenor,segmentasi_debitur_cd,pekerjaan_cd,debtor_category_cd,sumber_penghasilan_cd,jumlah_tanggungan)"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try (
                        Connection conn = DatabaseHelper.getInstance().getConnection();
                        PreparedStatement statement = conn.prepareStatement(sqlRealisasi,Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1,id + 1);
                    statement.setInt(2,1);
                    statement.setInt(3, 1);
                    statement.setDate(4, Date.valueOf(LocalDate.now()));
                    statement.setDate(5, Date.valueOf(LocalDate.now()));
                    statement.setString(6, "1");
                    if (keterangan.equals("OK")) {
                        statement.setString(7,"200");
                    } else {
                        statement.setString(7,"400");
                    }
                    statement.setString(8, keterangan);
                    statement.setString(9, list[0]);
                    statement.setString(10, list[1]);
                    statement.setString(11, list[2]);
                    statement.setString(12, list[3]);
                    statement.setString(13, list[4]);
                    statement.setString(14, list[5]);
                    statement.setString(15, list[6]);
                    statement.setString(16, list[7]);
                    statement.setString(17, list[8]);
                    statement.setString(18, list[9]);
                    statement.setString(19, list[10]);
                    statement.setString(20, list[11]);
                    statement.setString(21, list[12]);
                    statement.setString(22, list[13]);
                    statement.setString(23, list[14]);
                    statement.setString(24, list[15]);
                    statement.setDate(25, convertStringToSQLDate(list[16], "ddMMyyyy"));
                    statement.setDate(34, convertStringToSQLDate(list[25], "ddMMyyyy"));
                    statement.setDate(37, convertStringToSQLDate(list[28], "ddMMyyyy"));
                    statement.setDate(39, convertStringToSQLDate(list[30], "ddMMyyyy"));
                    statement.setDate(46, convertStringToSQLDate(list[37], "ddMMyyyy"));
                    statement.setDate(47, convertStringToSQLDate(list[38], "ddMMyyyy"));
                    statement.setDate(57, convertStringToSQLDate(list[48], "ddMMyyyy"));
                    statement.setString(26, list[17]);
                    statement.setString(27, list[18]);
                    statement.setString(28, list[19]);
                    statement.setString(29, list[20]);
                    statement.setString(30, list[21]);

                    if (CSVService.checkvalidationPenghasilanKotorPerBulan(list,"Penghasilan Kotor per Bulan").equals("VALID")) {
                        if (!list[22].isEmpty()) {
                            statement.setDouble(31, Double.parseDouble(list[22]));
                        } else {
                            statement.setDouble(31, 0.0d);
                        }
                    } else {
                        statement.setDouble(31, 0.0d);
                    }

                    if (CSVService.checkvalidationPlafon(list,"Jenis Kredit").equals("VALID")) {
                        statement.setDouble(43, Double.parseDouble(list[34]));
                    } else {
                        statement.setDouble(43, 0);
                    }

                    if (CSVService.checkvalidationInterestRate(list,"Interest Rate").isEmpty()) {
                        statement.setDouble(44, Double.parseDouble(list[35]));
                    } else {
                        statement.setDouble(44, 0);
                    }

                    if (CSVService.checkvalidationJangkaWaktu(list, "Jangka Waktu").equals("VALID")) {
                        statement.setInt(41, Integer.parseInt(list[32]));
                    } else {
                        statement.setInt(41, 0);
                    }

                    if (CSVService.checkvalidationOmzet(list,"Omzet").equals("VALID")) {
                        if (!list[41].isEmpty()) {
                            statement.setDouble(50, Double.parseDouble(list[41]));
                        } else {
                            statement.setDouble(50, 0);
                        }
                    } else {
                        statement.setDouble(50, 0);
                    }

                    if (CSVService.checkvalidationPenghasilanKotorPerTahun(list,"Penghasilan Kotor per Tahun").equals("VALID")) {
                        if (!list[44].isEmpty()) {
                            statement.setDouble(53, Double.parseDouble(list[44]));
                        } else {
                            statement.setDouble(53, 0);
                        }
                    } else {
                        statement.setDouble(53, 0);
                    }

                    if (CSVService.checkvalidationOriginalLoanAmount(list, "Original Loan Amount").equals("VALID")) {
                        statement.setDouble(56, Double.parseDouble(list[47]));
                    } else {
                        statement.setDouble(56, 0);
                    }

                    if (CSVService.checkvalidationTenor(list[49]).equals("VALID")) {
                        statement.setInt(58, Integer.parseInt(list[49]));
                    } else {
                        statement.setInt(58, 0);
                    }

                    if (CSVService.checkvalidationJumlahTanggungan(list,"Jumlah Tanggungan").isEmpty()) {
                        statement.setInt(63, Integer.parseInt(list[54]));
                    } else {
                        statement.setInt(63, 0);
                    }

                    statement.setString(32, list[23]);
                    statement.setString(33, list[24]);
                    statement.setString(35, list[26]);
                    statement.setString(36, list[27]);
                    statement.setString(38, list[29]);
                    statement.setString(40, list[31]);
                    statement.setString(42, list[33]);
                    statement.setString(45, list[36]);
                    statement.setString(48, list[39]);
                    statement.setString(49, list[40]);
                    statement.setString(51, list[42]);
                    statement.setString(52, list[43]);
                    statement.setString(54, list[45]);
                    statement.setString(55, list[46]);
                    statement.setString(59, list[50]);
                    statement.setString(60, list[51]);
                    statement.setString(61, list[52]);
                    statement.setString(62, list[53]);

                    statement.executeUpdate();
                } catch (SQLException ex) {
                    info("id for " + id + " message => " + ex.getMessage());
                }
            }
            break;
            case "OS_PENGURUS" : {
                String sqlPengurus = "INSERT INTO " + table + "(" +
                        "id,created_by,updated_by,created_date,updated_date,status,respon_status,keterangan_gagal,os_reafile_id,no_aplikasi,no_urut,jumlah_pengurus,jabatan_cd,pangsa_kepemilikan,bentuk_pengurus,modal_dasar,modal_disetor,modal_ditempatkan,npwp,nama,alamat,kelurahan,kecamatan,dati2,no_ktp,no_akte,tgl_lahir,tgl_akte,dati2_tempat_lahir,gender_pengurus_cd)"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                System.out.println(sqlPengurus);
                try (Connection conn = DatabaseHelper.getInstance().getConnection();
                     PreparedStatement statement = conn.prepareStatement(sqlPengurus,Statement.RETURN_GENERATED_KEYS);) {
                    statement.setInt(1,id + 1);
                    statement.setInt(2,1);
                    statement.setInt(3, 1);
                    statement.setDate(4, Date.valueOf(LocalDate.now()));
                    statement.setDate(5, Date.valueOf(LocalDate.now()));
                    statement.setString(6, "1");

                    if (keterangan.equals("OK")) {
                        statement.setString(7,"200");
                    } else {
                        statement.setString(7,"400");
                    }
                    statement.setString(8, keterangan);
                    statement.setInt(9, 1);
                    statement.setString(10, list[0]);
                    if (CSVService.checkValidationNomorUrutPengurus(list,"Nomor Urut Pengurus").equals("VALID")) {
                        statement.setInt(11, Integer.parseInt(list[1]));
                    } else {
                        statement.setInt(11, 0);
                    }
                    if (CSVService.checkValidationJumlahPengurus(list,"Jumlah Pengurus").equals("VALID")) {
                        statement.setInt(12, Integer.parseInt(list[2]));
                    } else {
                        statement.setInt(12, 0);
                    }
                    statement.setString(13, list[3]);
                    if (CSVService.checkValidationPangsaKepemilikan(list, "Pangsa Kepemilikan").equals("VALID")) {
                        statement.setDouble(14, Double.parseDouble(list[4]));
                    } else {
                        statement.setDouble(14, 0);
                    }

                    statement.setString(15, list[5]);

                    if (CSVService.checkValidationModaldasar(list, "Modal dasar").equals("VALID")) {
                        statement.setDouble(16, Double.parseDouble(list[6]));
                    } else {
                        statement.setDouble(16, 0);
                    }

                    if (CSVService.checkValidationModaldisetor(list, "Modal disetor").equals("VALID")) {
                        statement.setDouble(17, Double.parseDouble(list[7]));
                    } else {
                        statement.setDouble(17, 0);
                    }

                    if (CSVService.checkValidationModalditempatkan(list, "Modal ditempatkan").equals("VALID")) {
                        statement.setDouble(18, Double.parseDouble(list[8]));
                    } else {
                        statement.setDouble(18, 0);
                    }

                    statement.setString(19, list[9]);
                    statement.setString(20, list[10]);
                    statement.setString(21, list[11]);
                    statement.setString(22, list[12]);
                    statement.setString(23, list[13]);
                    statement.setString(24, list[14]);
                    statement.setString(25, list[15]);
                    statement.setString(26, list[16]);
                    statement.setDate(27, convertStringToSQLDate(list[17], "ddMMyyyy"));
                    statement.setDate(28, convertStringToSQLDate(list[18], "ddMMyyyy"));
                    statement.setString(29, list[19]);
                    statement.setString(30, list[20]);
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    info("id for " + id + " message => " + ex.getMessage());
                }
            }
            break;
            case "OS_REPAYMENT" : {
                String sqlRepayment = "INSERT INTO " + table + "(" +
                        "id,created_by,updated_by,created_date,updated_date,status,respon_status,keterangan_gagal,nomor_aplikasi,nomor_pembayaran,tanggal_pambayaran,nominal_pokok,nominal_bunga,nominal_denda,pelunasan)"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try (
                        Connection conn = DatabaseHelper.getInstance().getConnection();
                        PreparedStatement statement = conn.prepareStatement(sqlRepayment,Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1,id + 1);
                    statement.setInt(2,1);
                    statement.setInt(3, 1);
                    statement.setDate(4, Date.valueOf(LocalDate.now()));
                    statement.setDate(5, Date.valueOf(LocalDate.now()));
                    statement.setString(6, "1");
                    if (keterangan.equals("OK")) {
                        statement.setString(7,"200");
                    } else {
                        statement.setString(7,"400");
                    }
                    statement.setString(8, keterangan);
                    statement.setString(9, list[0]);
                    statement.setString(10, list[1]);
                    statement.setDate(11, convertStringToSQLDate(list[2], "ddMMyyyy"));
                    if (CSVService.checkValidationNominal(list).equals("VALID")) {
                        statement.setInt(12, Integer.parseInt(list[3]));
                        statement.setInt(13, Integer.parseInt(list[4]));
                        statement.setInt(14, Integer.parseInt(list[5]));
                    } else {
                        statement.setInt(12, 0);
                        statement.setInt(13, 0);
                        statement.setInt(14,0);
                    }
                    statement.setString(15, list[6]);

                    statement.executeUpdate();
                } catch (SQLException ex) {
                    info("id for " + id + " message => " + ex.getMessage());
                }
            }
            break;
        }

    }

    public static void info(String message) {
        System.setProperty("log.property.location",
                System.getProperty("batch.csvfv.log.properties"));
        bLog = BatchLogger.BATCH_LOGGER;
        bLog.info(message);
    }


    public static java.util.Date convertStringToDate(String dateValue, String dateFormat) {
        java.util.Date retval = null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            if ("".equals(dateValue) || dateValue == null) {
                retval = null;
            } else {
                retval = sdf.parse(dateValue);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return retval;
    }


    public static String convertDateToString(java.util.Date dateValue, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String retval = (("".equals(dateValue) || null == dateValue) ? "" : sdf.format(dateValue));
        return retval;
    }
    public static java.sql.Date convertStringToSQLDate(String dateValue, String dateFormat) {
        java.sql.Date retval = null;
        String dateValueFinal = null;
        if (dateValue == null || ("".equals(dateValue))) {
            retval = null;
        } else {
            dateValueFinal = convertDateToString(convertStringToDate(dateValue, dateFormat),
                    "yyyy-MM-dd");
            retval = java.sql.Date.valueOf(dateValueFinal);
        }
        return retval;
    }
}
