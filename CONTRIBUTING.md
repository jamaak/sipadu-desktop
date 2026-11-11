# Panduan Kontribusi

## 1. Persiapan awal

```bash
git clone https://github.com/USERNAME/REPOSITORY.git
cd REPOSITORY
git switch develop
git pull origin develop
```

Ganti URL clone sesuai repository tim.

## 2. Membuat branch tugas

```bash
git switch -c feature/login-form
```

Gunakan pola:
- `feature/nama-fitur`
- `fix/nama-bug`
- `docs/nama-dokumentasi`
- `refactor/nama-modul`
- `test/nama-pengujian`

## 3. Siklus pengembangan

```bash
# Pastikan branch integrasi terbaru
git switch develop
git pull origin develop

# Kembali ke branch pekerjaan
git switch feature/login-form

# Integrasikan perubahan terbaru dari develop
git merge develop

# Setelah mengerjakan dan menguji kode
mvn clean test
git status
git add src/
git commit -m "feat: add login form"
git push -u origin feature/login-form
```

Kemudian buat Pull Request dari `feature/login-form` ke `develop`.

## 4. Sebelum membuat Pull Request

- [ ] Fitur sesuai issue/tugas.
- [ ] `mvn clean test` berhasil.
- [ ] Tidak ada file rahasia atau hasil build yang ikut ter-commit.
- [ ] Nama class, method, dan variabel jelas.
- [ ] Perubahan tidak mencampur banyak pekerjaan yang tidak berkaitan.
- [ ] Dokumentasi diperbarui bila diperlukan.

## 5. Setelah PR di-merge

```bash
git switch develop
git pull origin develop
git branch -d feature/login-form
```

Hapus branch remote setelah merge jika belum dihapus otomatis.
