//
// Created by 2024 dterazhao.
//

#pragma once

#include "heu/library/phe/encoding/encoding.h"
#include "heu/library/phe/phe.h"
#include "util/stopwatch.hpp"
#include "util/utils.h"

using SchemaType = heu::lib::phe::SchemaType;
using Ciphertext = heu::lib::phe::Ciphertext;
using Plaintext = heu::lib::phe::Plaintext;

class PheKit {
 private:
  std::shared_ptr<heu::lib::phe::HeKit> he_kit_;
  std::shared_ptr<heu::lib::phe::DestinationHeKit> dhe_kit_;
  std::shared_ptr<heu::lib::phe::Encryptor> encryptor_;
  std::shared_ptr<heu::lib::phe::Decryptor> decryptor_;
  std::shared_ptr<heu::lib::phe::Evaluator> evaluator_;
  std::shared_ptr<heu::lib::phe::PlainEncoder> encoder_;
  std::shared_ptr<heu::lib::phe::BatchEncoder> batch_encoder_;
  bool has_secret_key = true;
  StopWatch sw;

 protected:
  std::function<Plaintext(double)> encoder_f;
  std::function<double(const Plaintext &)> decoder_f;
  std::function<Plaintext(double, double)> batch_encoder_f;

  void init(std::shared_ptr<heu::lib::phe::HeKitPublicBase> he_kit, int64_t scale);

  template<typename ...ARGS>
  inline Ciphertext *encrypt(std::function<Plaintext(ARGS...)> encoder, ARGS ...args) {
    auto res = new Ciphertext();
    *res = encryptor_->Encrypt(encoder(std::forward<ARGS>(args)...));
    return res;
  }

  inline Ciphertext *encrypt(size_t size, std::function<Plaintext(int)> encoder) {
    sw.Mark("encrypt");
    auto res = new Ciphertext[size];
    ParallelFor(size, [&](int i) {
      res[i] = encryptor_->Encrypt(encoder(i));
    });
    sw.PrintWithMills("encrypt");

    return res;
  }

  template<typename T>
  inline T decrypt(const Ciphertext &ct, std::function<T(const Plaintext &)> decoder) {
    Plaintext out;
    decryptor_->Decrypt(ct, &out);
    return decoder(out);
  }

  template<typename T>
  inline T *decrypt(const Ciphertext *ct, size_t size, std::function<T(const Plaintext &)> decoder) {
    sw.Mark("decrypt");
    T *res = new T[size];
    ParallelFor(size, [&](int i) {
      res[i] = this->decrypt(ct[i], decoder);
    });
    sw.PrintWithMills("decrypt");

    return res;
  }

 public:
  PheKit(SchemaType schema_type, size_t key_size = 2048, int64_t scale = 1e6);
  PheKit(uint8_t schema_type, size_t key_size = 2048, int64_t scale = 1e6);
  PheKit(yacl::ByteContainerView pk_buffer, int64_t scale = 1e6);

  bool hasSecretKey() const;
  const std::shared_ptr<heu::lib::phe::PublicKey> &getPublicKey() const;
  const std::shared_ptr<heu::lib::phe::SecretKey> &getSecretKey() const;

  Ciphertext *encrypt(double data);
  Ciphertext *encrypt(double *data, size_t size);
  double decrypt(const Ciphertext &ct);
  double *decrypt(const Ciphertext *ct, size_t size);

  Ciphertext *encryptPair(double d1, double d2);
  Ciphertext *encryptPair(double *d1, double *d2, size_t size);
  double *decryptPair(const Ciphertext &ct);
  double **decryptPair(const Ciphertext *ct, size_t size);

  Ciphertext *add(const Ciphertext &ct1, const Ciphertext &ct2);
  void addInplace(Ciphertext &ct1, const Ciphertext &ct2);
};
