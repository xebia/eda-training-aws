import {RouterModule, Routes} from '@angular/router';
import {ListCustomerComponent} from "./views/list-customer/list-customer.component";
import {AddCustomerComponent} from "./views/add-customer/add-customer.component";
import {EditCustomerComponent} from "./views/edit-customer/edit-customer.component";

const routes: Routes = [
    {
        path: 'customer', children: [
            {path: 'add', component: AddCustomerComponent},
            {path: 'edit', component: EditCustomerComponent},
            {path: '', component: ListCustomerComponent}
        ]
    },
];

export const CustomerRouting = RouterModule.forChild(routes);
