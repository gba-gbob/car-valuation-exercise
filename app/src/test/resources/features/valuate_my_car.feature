Feature: perform car valuation

  Scenario Outline: Car search results should match expectations file
    Given Bob reads car registration numbers from <input_file>
    When Bob performs valuation search for each car
    Then Bob finds results matching <output_file>

    Examples: files containing non existing registration numbers
      | input_file                 | output_file                 |
      | "car_input.txt"            | "car_output.txt"            |

    Examples: edited files containing only existing registration numbers allowing test to pass
      | input_file                 | output_file                 |
      | "valid_only_car_input.txt" | "valid_only_car_output.txt" |