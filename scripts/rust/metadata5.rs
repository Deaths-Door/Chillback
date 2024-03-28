fn main() {
    let mut data : Vec<_> =TRACK_METADATA_STR.split("\n").filter_map(|s| {
        s.find(',').map(|comma_index|{
            let ns = &s[comma_index + 2..];
            let number = &ns[..ns.find(',').unwrap() - 1];
            let number = number.parse::<u16>().unwrap();
            (s,number)
        })
    }).collect();
    data.sort_by(|a,b| a.1.cmp(&b.1).reverse());

    let output = data.into_iter().map(|v| match v.0.is_empty() {
        true => "\n".to_string(),
        false =>format!("lazy {{ {} }}",&v.0[..v.0.len() - 2])
    }).collect::<Vec<String>>().join(",\n");
    println!("{output}");
}