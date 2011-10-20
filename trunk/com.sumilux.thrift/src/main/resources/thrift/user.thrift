namespace java com.sumilux.thrift

struct User {
  1: i64 userid,
  2: string username,
  3: string password
}
service IHello {
  void insertUser(1: User user),
  User queryUser(1: i64 userid)
}