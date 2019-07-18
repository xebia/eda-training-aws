import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Customer} from '../model/customer.model';
import {VersionAwareService} from "../../shared/version-aware-service";

@Injectable()
export class CustomerService extends VersionAwareService {
    constructor(private http: HttpClient) {
        super();
    }

    baseUrl: string = 'http://localhost:9020/customer-api';

    getCustomers() {
        return this.http.get<Customer[]>(this.versionedUrl(this.baseUrl, 'customers'));
    }

    getCustomerById(id: number) {
        return this.http.get<Customer>(this.versionedUrl(this.baseUrl, `customers/${id}`));
    }

    createCustomer(user: Customer) {
        return this.http.post(this.versionedUrl(this.baseUrl, 'customers'), user);
    }

    updateCustomer(user: Customer) {
        return this.http.put(this.versionedUrl(this.baseUrl, `customers/${user.id}`), user);
    }

    deleteCustomer(id: number) {
        return this.http.delete(this.versionedUrl(this.baseUrl, `customers/${id}`));
    }
}
