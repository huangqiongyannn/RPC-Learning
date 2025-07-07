package com.hqy.pojo;

import com.caucho.hessian.io.Serializer;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int age;

    // 必须要有无参构造器（Protostuff 反序列化需要）
    public User() {}

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter和Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age + '}';
    }

    // 重写equals方法，方便断言比较
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return age == user.age && (name != null ? name.equals(user.name) : user.name == null);
    }

    @Override
    public int hashCode() {
        int result = (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}
