import javax.inject._

import play.api.http.DefaultHttpFilters
import play.filters.gzip.GzipFilter
import play.filters.headers.SecurityHeadersFilter

class Filters @Inject() (securityHeadersFilter: SecurityHeadersFilter, gzipFilter: GzipFilter)
  extends DefaultHttpFilters(securityHeadersFilter, gzipFilter)
