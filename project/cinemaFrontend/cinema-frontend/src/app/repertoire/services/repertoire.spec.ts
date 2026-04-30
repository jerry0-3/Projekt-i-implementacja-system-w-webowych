import { TestBed } from '@angular/core/testing';

import { Repertoire } from './repertoire';

describe('Repertoire', () => {
  let service: Repertoire;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Repertoire);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
