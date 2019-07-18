import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Customer} from '../model/customer.model';

@Injectable()
export class CustomerService {
    constructor(private http: HttpClient) {
    }

    baseUrl: string = 'http://localhost:9020/customer-api/v2/customers';

    getCustomers() {
        return this.http.get<Customer[]>(this.baseUrl);
    }

    getCustomerById(id: number) {
        return this.http.get<Customer>(`${this.baseUrl}/${id}`);
    }

    createCustomer(user: Customer) {
        return this.http.post(this.baseUrl, user);
    }

    updateCustomer(user: Customer) {
        return this.http.put(this.baseUrl + '/' + user.id, user);
    }

    deleteCustomer(id: number) {
        return this.http.delete(this.baseUrl + '/' + id);
    }
}
