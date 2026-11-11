# Java Swing Desktop App

Starter repository untuk aplikasi desktop Java Swing dengan Maven dan workflow Git tim kecil (2–5 orang).

## Prasyarat

- JDK 17 atau lebih baru
- Apache Maven 3.9+
- Git
- IDE pilihan tim (IntelliJ IDEA, Eclipse, atau VS Code dengan Java extensions)

Periksa instalasi:

```bash
java -version
mvn -version
git --version
```

## Menjalankan aplikasi

```bash
mvn clean test
mvn package
mvn exec:java -Dexec.mainClass="com.example.app.Main"
```

Perintah terakhir menggunakan Maven Exec Plugin yang sudah dikonfigurasi di `pom.xml`. Alternatifnya, jalankan class `com.example.app.Main` dari IDE setelah project di-import sebagai Maven project.

## Struktur direktori

```text
java-swing-git-workflow/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   ├── workflows/
│   │   └── maven-ci.yml
│   └── pull_request_template.md
├── docs/
│   ├── GIT_WORKFLOW.md
│   ├── TEAM_RULES.md
│   └── RELEASE.md
├── src/
│   ├── main/
│   │   ├── java/com/example/app/
│   │   │   ├── Main.java
│   │   │   └── ui/MainFrame.java
│   │   └── resources/
│   └── test/java/com/example/app/
│       └── SampleTest.java
├── .editorconfig
├── .gitignore
├── CONTRIBUTING.md
├── pom.xml
└── README.md
```

## Aturan penting

- `main` selalu stabil; hanya menerima Pull Request yang lolos CI dan review.
- `develop` menjadi branch integrasi pekerjaan harian.
- Setiap tugas dikerjakan pada branch `feature/*`, `fix/*`, atau `docs/*`.
- Jangan commit `target/`, file IDE, password, token, atau konfigurasi lokal.
- Jangan mengerjakan langsung di `main` atau `develop`.
- Satu PR sebaiknya fokus pada satu fitur/perbaikan dan mudah direview.

Lihat folder `docs/` dan `CONTRIBUTING.md` untuk panduan lengkap.
