# Çevrimiçi Sipariş Takip Sistemi

Bu proje, Spring Boot ve Java kullanılarak geliştirilen bir çevrimiçi sipariş takip sistemi. Müşterilere çeşitli ürünleri sipariş etme ve siparişlerini takip etme imkanı sağlar.

## Proje Yapısı

Proje, sistemin belirli işlevselliğine ilişkin sınıfları içeren birkaç pakete bölünmüştür.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.order:** Bu paket, sipariş yönetimi ile ilgili sınıfları içerir. Order entity, HTTP isteklerini işlemek için OrderController, iş mantığı için OrderService ve veritabanı işlemleri için OrderRepository içerir.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.product:** Bu paket, ürün yönetimi ile ilgili sınıfları içerir. Product entity, HTTP isteklerini işlemek için ProductController, iş mantığı için ProductService ve veritabanı işlemleri için ProductRepository içerir.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.customer:** Bu paket, müşteri yönetimi ile ilgili sınıfları içerir. Customer entity, HTTP isteklerini işlemek için CustomerController, iş mantığı için CustomerService ve veritabanı işlemleri için CustomerRepository içerir.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.cart:** Bu paket, alışveriş sepeti yönetimi ile ilgili sınıfları içerir. Cart entity, HTTP isteklerini işlemek için CartController, iş mantığı için CartService ve veritabanı işlemleri için CartRepository içerir.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.cartitem:** Bu paket, sepet öğesi yönetimi ile ilgili sınıfları içerir. CartItem entity ve CartItemRepository içerir.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.security:** Bu paket, uygulamanın güvenlik yapılandırması ile ilgili sınıfları içerir.

## Kurulum

1. Depoyu yerel makinenize klonlayın.
2. Projeyi tercih ettiğiniz IDE'de açın (IntelliJ IDEA önerilir).
3. `application.properties` dosyasını veritabanı kimlik bilgilerinizle güncelleyin.
4. Uygulamayı başlatmak için `OnlineOrderTrackingSystemApplication` sınıfını çalıştırın.

## Kullanım

### Ürün Yönetimi

- Yeni bir ürün eklemek için, ürün detaylarını istek gövdesinde kullanarak bir POST isteği gönderin: `/api/products`.
- Tüm ürünleri almak için bir GET isteği gönderin: `/api/products`.
- Belirli bir kategoriye ve fiyat aralığına göre ürünleri aramak için bir GET isteği gönderin: `/api/products/category`. Kategori, minimum fiyat ve maksimum fiyatı istek parametreleri olarak gönderin.
- Ürünün ID'sine göre almak için bir GET isteği gönderin: `/api/products/{id}`.
- Bir ürünü güncellemek için, yeni ürün detaylarını istek gövdesinde kullanarak bir PUT isteği gönderin: `/api/products/{id}`.
- Bir ürünü silmek için bir DELETE isteği gönderin: `/api/products/{id}`.

### Sipariş Yönetimi

- Yeni bir sipariş vermek için, sipariş detaylarını istek gövdesinde kullanarak bir POST isteği gönderin: `/api/orders`.
- Tüm siparişleri almak için bir GET isteği gönderin: `/api/orders`.
- Siparişin ID'sine göre almak için bir GET isteği gönderin: `/api/orders/track/{id}`.
- Bir siparişi güncellemek için, yeni sipariş detaylarını istek gövdesinde kullanarak bir PUT isteği gönderin: `/api/orders/{id}`.
- Bir siparişi iptal etmek ve siparişin miktarı kadar ürün stoğunu artırmak için bir DELETE isteği gönderin: `/api/orders/cancel/{id}`.
- Bir siparişin tahmini teslim tarihini almak için siparişin ID'sine göre bir GET isteği gönderin: `/api/orders/track/{id}/estimatedDeliveryDate`.

### Müşteri Yönetimi

- Yeni bir müşteri kaydı oluşturmak için, müşteri detaylarını istek gövdesinde kullanarak bir POST isteği gönderin: `/api/register`.
- Giriş yapmak için, müşterinin e-posta ve şifresini istek gövdesinde kullanarak bir POST isteği gönderin: `/api/login`.
- Tüm müşterileri almak için bir GET isteği gönderin: `/api/customers`. 

### Sepet Yönetimi

- Ürünü sepete eklemek için, istek gövdesinde ürün ayrıntılarını içeren bir POST isteği gönderin: `/api/cart/{ürünId}/{miktar}`.

### Test

Proje, `OrderService`, `OrderController`, `ProductService`, `ProductController`, `CustomerService`, `CustomerController`, `CartService` ve `CartController` sınıfları için birim testleri içerir. Bu testleri IDE'nizin yerleşik test çalıştırıcısını kullanarak çalıştırabilirsiniz.

### Güvenlik

Uygulama, temel güvenlik yapılandırması için Spring Security kullanmaktadır. SecurityConfig sınıfı güvenlik filtre zincirini yapılandırır ve `PCrypt` sınıfı şifre kodlaması için `BCryptPasswordEncoder` örneği sağlar.

### Veritabanı

Uygulama, veritabanı olarak PostgreSQL kullanmaktadır. `application.properties` dosyası veritabanı yapılandırmasını içerir. OrderRepository, ProductRepository, CustomerRepository, CartRepository ve CartItemRepository arabirimleri CRUD işlemleri için JpaRepository'yi genişletir.

### Günlükleme

Uygulama, günlükleme için SLF4J kullanmaktadır. OrderService, ProductService, CustomerService ve CartService sınıfları, mesajları kaydetmek için bir Logger örneği içerir.

### Bağımlılıklar

Proje, web, veri JPA, güvenlik ve actuator için Spring Boot Starter gibi çeşitli bağımlılıklar kullanmaktadır. Sunucu tarafında Java şablon motoru için Thymeleaf, kod tekrarını azaltmak için Lombok ve PostgreSQL veritabanı ile bağlantı için PostgreSQL JDBC Sürücüsü gibi bağımlılıklar içerir. Bu bağımlılıklar, Maven kullanılarak yönetilir ve `pom.xml` dosyasında belirtilir.

### Docker

Bu uygulama, Docker kullanarak dağıtılabilir. Docker, uygulamaları dağıtmak için bir platform sağlar. Docker kullanmak için, projenin kök dizininde bir Dockerfile oluşturun. Dockerfile, Docker imajınızın nasıl oluşturulacağını belirtir. Docker imajınızı oluşturmak ve çalıştırmak için aşağıdaki komutları kullanabilirsiniz:

```
docker build -t online-order-tracking-system .    
```
```
docker run -p 8000:8080 online-order-tracking-system
```

Bu komutlar uygulamanızı Docker konteyneri olarak çalıştırır ve 8000 numaralı bağlantı noktasını dinler.
