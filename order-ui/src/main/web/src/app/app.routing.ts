import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
    {path: '', redirectTo: '/customer', pathMatch: 'full'},
];

export const AppRouting = RouterModule.forRoot(routes);
