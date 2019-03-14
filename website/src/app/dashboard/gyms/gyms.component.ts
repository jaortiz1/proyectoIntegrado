import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';
import { GymService } from 'src/app/services/gym.service';
import { MatDialog, MatSnackBar, MatPaginator, MatTableDataSource } from '@angular/material';
import { GymResponse } from 'src/app/interfaces/gym-response';
import { DeleteGymDialogComponent } from 'src/app/dialogs/delete-gym-dialog/delete-gym-dialog.component';

@Component({
  selector: 'app-gyms',
  templateUrl: './gyms.component.html',
  styleUrls: ['./gyms.component.scss']
})
export class GymsComponent implements OnInit {

  constructor(private router: Router, private authService: AuthenticationService, private gymService: GymService,
    public snackBar: MatSnackBar, public dialog: MatDialog) { }
  displayedColumns: string[] = ['picture', 'name', 'address', 'actions'];

  dataSource;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {

    if (this.authService.getToken() == null) {
      this.router.navigate(['/']);
    } else {
      this.getAllGyms();
    }

  }
  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }
  getAllGyms() {
    this.gymService.getAllGyms().subscribe(gymList => {
      console.log('aqui')
      this.dataSource = new MatTableDataSource(gymList.rows);
      this.dataSource.paginator = this.paginator;
      console.log(gymList.rows);
    }, error => {
      this.snackBar.open('Error obtaining gyms', 'Close', {
        duration: 3000,
        verticalPosition: 'top'
      });
    });
  }
  openDialogDeleteGym(g: GymResponse) {
    const dialogDeleteGym = this.dialog.open(DeleteGymDialogComponent, { data: { gym: g } });
    dialogDeleteGym.afterClosed().subscribe(result => {
      this.getAllGyms();
    });
  }
}
