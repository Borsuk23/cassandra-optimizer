import { CassandraOptimizerPage } from './app.po';

describe('cassandra-optimizer App', () => {
  let page: CassandraOptimizerPage;

  beforeEach(() => {
    page = new CassandraOptimizerPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
