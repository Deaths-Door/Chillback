
fn main() {
    let mut stilltodo = vec![];
    let output = DATA.split("\n")
        .map(|s| match s.matches(",").count() {
            4 => {
                stilltodo.push(s[s.find("FieldKey.").unwrap() + "FieldKey.".len()..s.find(",").unwrap()].to_string());
                s.to_string()
            },
            _ => s.to_string()
        })
        .collect::<Vec<_>>()
        .join("\n");

    println!("len={}{:?}",stilltodo.len(),stilltodo);
}