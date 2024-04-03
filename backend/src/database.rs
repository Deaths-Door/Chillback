use std::{ops::Deref, sync::Mutex, time::Duration};

use once_cell::sync::Lazy;
use r2d2::Pool;
use r2d2_sqlite::SqliteConnectionManager;

static CACHE_INSTANCE : Lazy<Mutex<Pool<SqliteConnectionManager>>> = Lazy::new(||{
    let manager = SqliteConnectionManager::file("file.db");

    let pool = Pool::builder()
        .idle_timeout(Duration::from_secs(60).into())
        .build(manager)
        .unwrap();

    Mutex::new(pool)
});

pub fn cache_instance<'a>() -> &'a Mutex<Pool<SqliteConnectionManager>> {
    crate::database::CACHE_INSTANCE.deref()
}