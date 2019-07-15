import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Customer} from "../model/user.model";
import {of} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class UserService {
  constructor(private http: HttpClient) { }
  baseUrl: string = 'http://localhost:9020/customer-api/v1/customers';

  getCustomers() {
   return this.http.get<Customer[]>(this.baseUrl).pipe(
       map(messages => messages.sort((a1: Customer, a2: Customer) => a1.id - a2.id ))
   )
  }

  getUserById(id: number) {
    return this.http.get<Customer>(this.baseUrl + '/' + id);
  }

  createUser(user: Customer) {
    return this.http.post(this.baseUrl, user);
  }

  updateUser(user: Customer) {
    return this.http.put(this.baseUrl + '/' + user.id, user);
  }

  deleteUser(id: number) {
    return this.http.delete(this.baseUrl + '/' + id);
  }
}
