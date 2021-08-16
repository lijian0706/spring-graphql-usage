
## 一、GraphQL简介
GraphQL是一种API查询语言，与SQL和数据库无关，但是两者有相似性，SQL是数据库查询语言，而GraphQL是API查询语言。它允许客户端定义响应结构(返回的字段及其类型)，服务端接收请求后将返回指定的响应参数。例如下面请求示例要求返回id为1000的human的name和height：
```
query { # query默认可以省略不写
  human(id: "1000") {
    name
    height
  }
}
```
下面是服务端响应：
```
{
  "data": {
    "human": {
      "name": "Luke Skywalker",
      "height": 1.72
    }
  }
}
```
### 操作类型
GraphQL支持三种类型的操作：query、mutation、subscription。query用于查询，mutation用于修改或新增，subscription用于订阅。

### schema文件
schema文件定义了客户端读写数据的类型，GraphQL支持两种数据类型：Scalar、Object
- scalar type即基本数据类型，包括：String,Int,Float,Boolean,ID，enum。其中ID是一种特殊的String，它表示字段是唯一的(即使服务端数据非String，GraphQL也会序列化成String)。
- Object type即对象类型

### GraphQL vs REST
![image](https://mobilelive.ca/wp-content/uploads/2020/11/GraphQL_Image1.png)

REST存在的问题：
- 会使客户端与服务端产生多次交互问题，REST以资源为导向，因此在面对复杂场景查询，例如需要多个资源拼凑时，需要将原本一个请求拆分成多个(当然在实际实现时也可以做成一个请求，但是这不符合REST风格)
- 存在过度获取(over-fetching)/获取不足(under-fetching)的问题，参数要么多给，要么少给，极少情况下接口给的参数刚好满足客户端需求

GraphQL可以解决REST以上痛点，它要求客户端指定自己需要的参数，服务端按照客户端的需求进行返回，不多不少。

但GraphQL目前也存在自身的一些问题：
- 对文件上传支持不好，解决方案可参考：https://www.apollographql.com/blog/backend/file-uploads/file-upload-best-practices/
- caching、security支持不够
- 稳定性不如REST，REST由2000年提出，已经过市场20年的检验，毫无疑问是极为健壮的，市场占有率也碾压GraphQL。
- 学习曲线较为困难

总结：GraphQL是一种查询语言、一种规范，它虽然可以解决REST的一些痛点，但它还存在着一些不完美的地方，就目前来说，GraphQL可以看做REST的补充，完全可以在项目中同时使用两者，但对于健壮性要求高的系统(例如金融领域)，还是推荐使用REST。

## 二、在Spring Boot中使用GraphQL
截止目前，`spring-graphql`项目还处于里程碑版本，教程按照里程碑版本进行介绍，正式版发布后依赖包、注解可能会有些变化。
下面会以简单的Human和Car的例子介绍在Spring Boot中如何使用GraphQL。

### 依赖及核心注解介绍
由于spring-graphql还处于里程碑版本，还没有发布到maven中心库中，因此只能从spring的依赖仓库中拉取相关依赖，gradle配置如下：
```
repositories {
	maven { url "https://repo.spring.io/artifactory/libs-snapshot-local/" }
	mavenCentral()
}

implementation 'org.springframework.experimental:graphql-spring-boot-starter:1.0.0-20210811.043505-62'
```
spring-graphql中定义的核心注解如下：
- `@GraphQlController`:申明该类是GraphQL应用中的控制器
- `@QueryMapping`:申明该类或方法使用GraphQL的query操作，等同于`@SchemaMapping(typeName = "Query")`，类似于`@GetMapping`
- `@Argument`:申明该参数是GraphQL应用的入参
- `@MutationMapping`:申明该类或方法使用GraphQL的mutation操作，等同于`@SchemaMapping(typeName = "Mutation")`
- `@SubscriptionMapping`:申明该类或方法使用GraphQL的subscription操作，等同于`@SchemaMapping(typeName = "Subscription")`
- `@SchemaMapping`:指定GraphQL操作类型的注解，类似`@RequestMapping`

### 例子说明
以Human和Car进行举例说明，下面是Human和Car实体类结构：
```
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Human {
    @Id
    private Long id;
    private String cardNum; // 身份证号
    private String name;
    private Integer age;
    @Embedded
    private Address address;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Address {
        private String province;
        private String city;
        private String addr;
    }

    public void modifyName(String name) {
        this.name = name;
    }
}

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    @Id
    private Long id;
    private String plateNum; // 车牌号
    private String ownerCardNum; // 车主身份证号
}
```

### 新建schema
在项目resources/graphql目录下新建`schema.graphqls`文件(支持扩展名为*.graphqls/*.graphql/*.gql/*.gqls的schema文件)，该文件用于指定GraphQL的schema，所有定义的接口和类型都在记录在这个文件中。

### 定义controller
HumanController:
```
@GraphQlController
public class HumanController {

    private final HumanService humanService;

    public HumanController(HumanService humanService) {
        this.humanService = humanService;
    }

    @QueryMapping
    public List<Human> list(){
        return humanService.list();
    }

    @QueryMapping
    public Human details(@Argument Long id){
        return humanService.details(id);
    }

    @MutationMapping
    public Human modifyName(@Argument HumanUpdateCmd cmd){
        return humanService.modifyName(cmd.getId(), cmd.getName());
    }
}
```
CarController:
```
@GraphQlController
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @QueryMapping(value = "carList")
    public List<Car> list(@Argument String ownerCardNum){
        return carService.list(ownerCardNum);
    }
}
```

### 定义schema
```
type Query{
    details(id:ID!): Human # 根据ID查询Human详情，Human类型定义在下面
    list: [Human] # 查询所有Human
    carList(ownerCardNum:String!): [Car] # 根据身份证号查询车辆
    car(id:ID!): Car
}

type Human{
    id: ID! # Human id，不可为空
    cardNum: String! # 身份证号，不可为空
    name: String! # 名字，不可为空
    age: Int # 年龄
    address: Address # 住址，Address类型定义在下面
}

type Address{
    province: String # 省份
    city: String # 城市
    addr: String # 地址详情
}

type Car{
    plateNum: String! # 车牌号
    ownerCardNum: String # 车主身份证号
}

type Mutation{
    modifyName(cmd:HumanUpdateCmd!): Human # 入参名称需要与接口入参名保持一致
}

input HumanUpdateCmd{ # 接口入参需要使用input修饰
    id: ID!
    name: String!
}
```

### 调用接口
启动项目后，可通过Postman来请求接口，也可通过GraphiQL来请求。GraphiQL是一个帮助我们构造GraphQL请求的工具，它内嵌到我们项目中，因此只需访问http://localhost:8080/graphiql即可使用，下面是graphiql界面截图
![image](https://raw.githubusercontent.com/lijian0706/spring-graphql-usage/main/img/graphiql.png)

##### 无入参的list接口
请求(只要求返回name和province)：
```
{
  list{
    name
    address{
      province
    }
  }
}
```
响应：
```
{
  "data": {
    "list": [
      {
        "name": "张三",
        "address": {
          "province": "安徽省"
        }
      },
      {
        "name": "李四",
        "address": {
          "province": "安徽省"
        }
      }
    ]
  }
}
```
##### 带入参的details接口
请求：
```
{
  details(id:1){
    name
  }
}
```
响应：
```
{
  "data": {
    "details": {
      "name": "张三"
    }
  }
}
```

##### 同时返回Human和Car数据
与REST不同，GraphQL可以通过一次请求同时获取到Human和Car数据。
请求：
```
{
  details(id:1){
    name
  }
  carList(ownerCardNum:"1"){
    plateNum
  }
}
```
响应：
```
{
  "data": {
    "details": {
      "name": "张三"
    },
    "carList": [
      {
        "plateNum": "皖A1111"
      }
    ]
  }
}
```

##### mutation用法
请求：
```
mutation($cmd: HumanUpdateCmd!){
  modifyName(cmd:$cmd){ # 入参名需要与schema中定义的入参名保持一致
    id
    name
  }
}
```
响应：
```
{
  "data": {
    "modifyName": {
      "id": "1",
      "name": "张四"
    }
  }
}
```

##### subscription用法
由于GraphiQL不支持subscription，因此需要编写前端代码，在此不做介绍，具体可查看官方提供的例子:https://github.com/spring-projects/spring-graphql/tree/main/samples/webflux-websocket

##### 完整示例代码
- https://github.com/lijian0706/spring-graphql-usage